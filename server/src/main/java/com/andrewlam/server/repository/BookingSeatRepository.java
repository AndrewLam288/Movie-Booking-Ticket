package com.andrewlam.server.repository;

import com.andrewlam.server.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    @Query("""
        select bs.seat.id
        from BookingSeat bs
        where bs.showtime.id = :showtimeId
    """)
    List<Long> findBookedSeatIdsByShowtimeId(Long showtimeId);

    @Query("""
        select count(bs) > 0
        from BookingSeat bs
        where bs.showtime.id = :showtimeId
          and bs.seat.id = :seatId
    """)
    boolean existsBookedSeat(Long showtimeId, Long seatId);

    @Query("""
        select bs.seat.id
        from BookingSeat bs
        where bs.showtime.id = :showtimeId
          and bs.seat.id in :seatIds
    """)
    List<Long> findBookedSeatIds(Long showtimeId, Collection<Long> seatIds);
}