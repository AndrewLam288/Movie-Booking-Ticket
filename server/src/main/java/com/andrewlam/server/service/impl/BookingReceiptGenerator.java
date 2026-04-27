package com.andrewlam.server.service.impl;

import com.andrewlam.server.model.Booking;
import com.andrewlam.server.model.BookingSeat;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class BookingReceiptGenerator {

    private static final DateTimeFormatter SHOWTIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    public BookingReceiptContent generate(Booking booking) {
        String movieTitle = booking.getShowtime().getMovie().getTitle();
        String cinemaName = booking.getShowtime().getRoom().getCinema().getName();
        String roomName = booking.getShowtime().getRoom().getName();
        String bookingCode = booking.getBookingCode();
        String showtimeText = booking.getShowtime().getStartTime().format(SHOWTIME_FORMAT);

        String seatsText = booking.getBookingSeats()
                .stream()
                .map(this::seatLabel)
                .collect(Collectors.joining(", "));

        String seatLinesHtml = booking.getBookingSeats()
                .stream()
                .map(seat -> "<li>" + seatLabel(seat) + " - $" + seat.getPrice() + "</li>")
                .collect(Collectors.joining());

        String subject = "Booking Confirmation - " + bookingCode;

        String plainText = """
                Thank you for your booking!

                Booking Code: %s
                Movie: %s
                Showtime: %s
                Cinema: %s
                Room: %s
                Seats: %s
                Total: $%s
                """.formatted(
                bookingCode,
                movieTitle,
                showtimeText,
                cinemaName,
                roomName,
                seatsText,
                booking.getTotalAmount()
        );

        String html = """
                <html>
                  <body style="font-family: Arial, sans-serif; color: #111827;">
                    <h2>Thank you for your booking!</h2>
                    <p>Your booking has been confirmed.</p>

                    <h3>Booking Receipt</h3>
                    <p><strong>Booking Code:</strong> %s</p>
                    <p><strong>Movie:</strong> %s</p>
                    <p><strong>Showtime:</strong> %s</p>
                    <p><strong>Cinema:</strong> %s</p>
                    <p><strong>Room:</strong> %s</p>

                    <p><strong>Seats:</strong></p>
                    <ul>%s</ul>

                    <p><strong>Total:</strong> $%s</p>
                  </body>
                </html>
                """.formatted(
                bookingCode,
                movieTitle,
                showtimeText,
                cinemaName,
                roomName,
                seatLinesHtml,
                booking.getTotalAmount()
        );

        return new BookingReceiptContent(subject, plainText, html);
    }

    private String seatLabel(BookingSeat bookingSeat) {
        return bookingSeat.getSeat().getSeatRow() + bookingSeat.getSeat().getSeatNumber();
    }
}