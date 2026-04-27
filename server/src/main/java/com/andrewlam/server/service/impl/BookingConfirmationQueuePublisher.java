package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.event.BookingConfirmedEvent;
import com.andrewlam.server.config.BookingConfirmationQueueProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookingConfirmationQueuePublisher {

    private static final Logger log = LoggerFactory.getLogger(BookingConfirmationQueuePublisher.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final BookingConfirmationQueueProperties queueProperties;

    public BookingConfirmationQueuePublisher(
            StringRedisTemplate stringRedisTemplate,
            BookingConfirmationQueueProperties queueProperties
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.queueProperties = queueProperties;
    }

    public RecordId enqueue(BookingConfirmedEvent event) {
        RecordId recordId = stringRedisTemplate.opsForStream().add(
                StreamRecords.string(
                        Map.of("bookingId", String.valueOf(event.bookingId()))
                ).withStreamKey(queueProperties.getStreamKey())
        );

        log.info("Queued booking confirmation job. bookingId={}, recordId={}", event.bookingId(), recordId);
        return recordId;
    }
}