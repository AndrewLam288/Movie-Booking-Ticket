
CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    rating VARCHAR(20),
    poster_url VARCHAR(500),
    trailer_url VARCHAR(500),
    release_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'COMING_SOON',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_movies_duration CHECK (duration_minutes > 0),
    CONSTRAINT chk_movies_status CHECK (status IN ('COMING_SOON', 'NOW_SHOWING', 'ENDED'))
);