package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.model.Booking;
import com.andrewlam.server.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingConfirmationProcessor {

    private final BookingRepository bookingRepository;
    private final BookingReceiptGenerator bookingReceiptGenerator;
    private final BookingConfirmationEmailService bookingConfirmationEmailService;

    public BookingConfirmationProcessor(
            BookingRepository bookingRepository,
            BookingReceiptGenerator bookingReceiptGenerator,
            BookingConfirmationEmailService bookingConfirmationEmailService
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingReceiptGenerator = bookingReceiptGenerator;
        this.bookingConfirmationEmailService = bookingConfirmationEmailService;
    }

    @Transactional(readOnly = true)
    public void process(Long bookingId) {
        Booking booking = bookingRepository.findByIdWithDetails(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for queue processing."));

        BookingReceiptContent receiptContent = bookingReceiptGenerator.generate(booking);
        bookingConfirmationEmailService.sendBookingConfirmation(booking, receiptContent);
    }
}