package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.event.BookingConfirmedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class BookingConfirmationQueueEventListener {

    private final BookingConfirmationQueuePublisher queuePublisher;

    public BookingConfirmationQueueEventListener(BookingConfirmationQueuePublisher queuePublisher) {
        this.queuePublisher = queuePublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        queuePublisher.enqueue(event);
    }
}