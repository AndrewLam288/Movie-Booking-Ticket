CREATE TABLE seat_holds (
                            id BIGSERIAL PRIMARY KEY,
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            showtime_id BIGINT NOT NULL,
                            seat_id BIGINT NOT NULL,
                            client_session_id VARCHAR(100) NOT NULL,
                            expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            CONSTRAINT fk_seat_holds_showtime
                                FOREIGN KEY (showtime_id) REFERENCES showtimes(id),
                            CONSTRAINT fk_seat_holds_seat
                                FOREIGN KEY (seat_id) REFERENCES seats(id),
                            CONSTRAINT uk_seat_hold_showtime_seat
                                UNIQUE (showtime_id, seat_id)
);

CREATE INDEX idx_seat_holds_showtime_id
    ON seat_holds(showtime_id);

CREATE INDEX idx_seat_holds_seat_id
    ON seat_holds(seat_id);

CREATE INDEX idx_seat_holds_expires_at
    ON seat_holds(expires_at);