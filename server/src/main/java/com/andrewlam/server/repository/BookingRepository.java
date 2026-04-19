package com.andrewlam.server.repository;

import com.andrewlam.server.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        select b
        from Booking b
        where b.bookingCode = :bookingCode
          and b.user.id = :userId
    """)
    Optional<Booking> findMyBookingByCode(String bookingCode, Long userId);
}