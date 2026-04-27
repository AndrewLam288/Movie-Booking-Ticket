package com.andrewlam.server.service.impl;

public record BookingReceiptContent(
        String subject,
        String plainText,
        String html
) {
}