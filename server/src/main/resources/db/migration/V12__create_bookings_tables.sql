CREATE TABLE bookings (
                          id BIGSERIAL PRIMARY KEY,
                          booking_code VARCHAR(32) NOT NULL UNIQUE,
                          user_id BIGINT NOT NULL,
                          showtime_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          total_amount NUMERIC(10, 2) NOT NULL,
                          booked_at TIMESTAMP WITH TIME ZONE NOT NULL,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_bookings_user
                              FOREIGN KEY (user_id) REFERENCES users(id),

                          CONSTRAINT fk_bookings_showtime
                              FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
);

CREATE TABLE booking_seats (
                               id BIGSERIAL PRIMARY KEY,
                               booking_id BIGINT NOT NULL,
                               showtime_id BIGINT NOT NULL,
                               seat_id BIGINT NOT NULL,
                               price NUMERIC(10, 2) NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_booking_seats_booking
                                   FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,

                               CONSTRAINT fk_booking_seats_showtime
                                   FOREIGN KEY (showtime_id) REFERENCES showtimes(id),

                               CONSTRAINT fk_booking_seats_seat
                                   FOREIGN KEY (seat_id) REFERENCES seats(id),

                               CONSTRAINT uk_booking_seat_per_showtime UNIQUE (showtime_id, seat_id)
);