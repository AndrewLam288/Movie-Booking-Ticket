package com.andrewlam.server.repository;

import com.andrewlam.server.model.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {
            "user",
            "showtime",
            "showtime.movie",
            "showtime.room",
            "showtime.room.cinema",
            "bookingSeats",
            "bookingSeats.seat"
    })
    @Query("select b from Booking b where b.id = :bookingId")
    Optional<Booking> findByIdWithDetails(@Param("bookingId") Long bookingId);

    @Query("""
        select b
        from Booking b
        where b.bookingCode = :bookingCode
          and b.user.id = :userId
    """)
    Optional<Booking> findMyBookingByCode(String bookingCode, Long userId);

    @EntityGraph(attributePaths = {"showtime", "bookingSeats", "bookingSeats.seat"})
    List<Booking> findAllByUserIdOrderByBookedAtDesc(Long userId);
}