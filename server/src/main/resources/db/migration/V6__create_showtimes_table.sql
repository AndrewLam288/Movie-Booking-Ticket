CREATE TABLE showtimes (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    language VARCHAR(50) NOT NULL DEFAULT 'ORIGINAL',
    format VARCHAR(50) NOT NULL DEFAULT 'TWO_D',
    base_price NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_showtimes_movie
        FOREIGN KEY (movie_id)
        REFERENCES movies(id),

    CONSTRAINT fk_showtimes_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id),

    CONSTRAINT chk_showtimes_time CHECK (end_time > start_time),
    CONSTRAINT chk_showtimes_price CHECK (base_price >= 0),
    CONSTRAINT chk_showtimes_format CHECK (format IN ('TWO_D', 'THREE_D', 'IMAX', 'FOUR_DX')),
    CONSTRAINT chk_showtimes_status CHECK (status IN ('SCHEDULED', 'CANCELLED', 'COMPLETED'))
);