CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    seat_row VARCHAR(5) NOT NULL,
    seat_number INT NOT NULL,
    seat_type VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_seats_room
                   FOREIGN KEY (room_id)
                   REFERENCES rooms(id),

    CONSTRAINT uq_seats_room_position UNIQUE (room_id, seat_row, seat_number),
    CONSTRAINT chk_seats_number CHECK (seat_number > 0),
    CONSTRAINT chk_seats_type CHECK (seat_type IN ('STANDARD', 'VIP', 'COUPLE'))
);