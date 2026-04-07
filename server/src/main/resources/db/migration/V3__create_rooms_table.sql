
CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    cinema_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    room_type VARCHAR(50) NOT NULL DEFAULT 'STANDARD',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rooms_cinema
                   FOREIGN KEY (cinema_id)
                   REFERENCES cinemas(id),

    CONSTRAINT uq_rooms_cinema_name UNIQUE (cinema_id, name),
    CONSTRAINT chk_rooms_capacity CHECK (capacity > 0),
    CONSTRAINT chk_rooms_type CHECK (room_type IN ('STANDARD', 'IMAX', 'VIP', '4DX'))
);