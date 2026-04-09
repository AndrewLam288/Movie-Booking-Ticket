
CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    language VARCHAR (100) NOT NULL,
    director VARCHAR (255),
    cast_members TEXT,
    poster_url VARCHAR(500),
    banner_url VARCHAR(500),
    trailer_url VARCHAR(500),
    release_date DATE,
    age_rating VARCHAR (50) NOT NULL DEFAULT 'G',
    status VARCHAR(50) NOT NULL DEFAULT 'COMING_SOON',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_movies_duration CHECK (duration_minutes > 0),
    CONSTRAINT chk_movies_age_rating CHECK (age_rating IN ('G', 'PG', 'R')),
    CONSTRAINT chk_movies_status CHECK (status IN ('COMING_SOON', 'NOW_SHOWING', 'ENDED'))
);