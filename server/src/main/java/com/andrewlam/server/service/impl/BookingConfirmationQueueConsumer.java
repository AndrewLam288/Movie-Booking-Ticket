package com.andrewlam.server.service.impl;

import com.andrewlam.server.config.BookingConfirmationQueueProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookingConfirmationQueueConsumer {

    private static final Logger log = LoggerFactory.getLogger(BookingConfirmationQueueConsumer.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final BookingConfirmationQueueProperties queueProperties;
    private final BookingConfirmationProcessor bookingConfirmationProcessor;

    public BookingConfirmationQueueConsumer(
            StringRedisTemplate stringRedisTemplate,
            BookingConfirmationQueueProperties queueProperties,
            BookingConfirmationProcessor bookingConfirmationProcessor
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.queueProperties = queueProperties;
        this.bookingConfirmationProcessor = bookingConfirmationProcessor;
    }

    @PostConstruct
    public void initializeConsumerGroup() {
        String streamKey = queueProperties.getStreamKey();

        if (streamKey == null || streamKey.isBlank()) {
            throw new IllegalStateException(
                    "Booking confirmation stream key is missing. Check app.queue.booking-confirmation.stream-key configuration."
            );
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(streamKey))) {
            stringRedisTemplate.opsForStream().add(
                    StreamRecords.string(Map.of("_init", "true")).withStreamKey(streamKey)
            );
        }

        try {
            stringRedisTemplate.opsForStream().createGroup(
                    streamKey,
                    ReadOffset.latest(),
                    queueProperties.getConsumerGroup()
            );
        } catch (Exception ex) {
            if (!isBusyGroupException(ex)) {
                throw ex;
            }
        }
    }

    private boolean isBusyGroupException(Throwable ex) {
        Throwable current = ex;

        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("BUSYGROUP")) {
                return true;
            }
            current = current.getCause();
        }

        return false;
    }

    @Scheduled(fixedDelayString = "${app.queue.booking-confirmation.fixed-delay-ms:3000}")
    public void pollQueue() {
        List<MapRecord<String, Object, Object>> records = stringRedisTemplate.opsForStream().read(
                Consumer.from(queueProperties.getConsumerGroup(), queueProperties.getConsumerName()),
                StreamReadOptions.empty().count(queueProperties.getBatchSize()),
                StreamOffset.create(queueProperties.getStreamKey(), ReadOffset.lastConsumed())
        );

        if (records == null || records.isEmpty()) {
            return;
        }

        for (MapRecord<String, Object, Object> record : records) {
            processRecord(record);
        }
    }

    private void processRecord(MapRecord<String, Object, Object> record) {
        try {
            Object bookingIdValue = record.getValue().get("bookingId");

            if (bookingIdValue == null) {
                acknowledgeAndDelete(record);
                return;
            }

            Long bookingId = Long.parseLong(bookingIdValue.toString());

            bookingConfirmationProcessor.process(bookingId);
            acknowledgeAndDelete(record);

            log.info("Processed booking confirmation job. bookingId={}, recordId={}",
                    bookingId, record.getId());
        } catch (Exception ex) {
            log.error("Failed to process booking confirmation queue record. recordId={}",
                    record.getId(), ex);
        }
    }

    private void acknowledgeAndDelete(MapRecord<String, Object, Object> record) {
        stringRedisTemplate.opsForStream().acknowledge(
                queueProperties.getStreamKey(),
                queueProperties.getConsumerGroup(),
                record.getId()
        );

        stringRedisTemplate.opsForStream().delete(
                queueProperties.getStreamKey(),
                record.getId()
        );
    }
}