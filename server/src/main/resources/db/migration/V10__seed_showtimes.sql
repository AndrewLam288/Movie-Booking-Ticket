INSERT INTO showtimes (
    created_at,
    updated_at,
    movie_id,
    room_id,
    format,
    status,
    start_time,
    end_time,
    base_price
)
WITH ordered_movies AS (
    SELECT
        id AS movie_id,
        duration_minutes,
        ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM movies
    WHERE status = 'NOW_SHOWING'
),
     generated_showtimes AS (
         SELECT
             movie_id,
             duration_minutes,
             rn,

             -- Showtime 1: Cinema 1 rooms (1-4)
             ((rn - 1) % 4) + 1 AS room_id_1,

    CASE ((rn - 1) % 4) + 1
    WHEN 1 THEN 'TWO_D'
    WHEN 2 THEN 'THREE_D'
    WHEN 3 THEN 'IMAX'
    WHEN 4 THEN 'FOUR_DX'
END AS format_1,

        CASE ((rn - 1) % 4) + 1
            WHEN 1 THEN 12.99
            WHEN 2 THEN 16.99
            WHEN 3 THEN 19.99
            WHEN 4 THEN 22.99
END AS base_price_1,

        CASE ((rn - 1) % 4) + 1
            WHEN 1 THEN TIME '11:00:00'
            WHEN 2 THEN TIME '13:30:00'
            WHEN 3 THEN TIME '16:30:00'
            WHEN 4 THEN TIME '20:00:00'
END AS start_clock_1,

        (1 + (((rn - 1) / 4) * 2)) AS start_day_offset_1,

        -- Showtime 2: Cinema 2 rooms (5-8)
        ((rn - 1) % 4) + 5 AS room_id_2,

        CASE ((rn - 1) % 4) + 5
            WHEN 5 THEN 'TWO_D'
            WHEN 6 THEN 'THREE_D'
            WHEN 7 THEN 'IMAX'
            WHEN 8 THEN 'FOUR_DX'
END AS format_2,

        CASE ((rn - 1) % 4) + 5
            WHEN 5 THEN 13.99
            WHEN 6 THEN 17.99
            WHEN 7 THEN 20.99
            WHEN 8 THEN 23.99
END AS base_price_2,

        CASE ((rn - 1) % 4) + 5
            WHEN 5 THEN TIME '12:15:00'
            WHEN 6 THEN TIME '15:00:00'
            WHEN 7 THEN TIME '18:15:00'
            WHEN 8 THEN TIME '21:00:00'
END AS start_clock_2,

        (2 + (((rn - 1) / 4) * 2)) AS start_day_offset_2

    FROM ordered_movies
)
SELECT
    NOW(),
    NOW(),
    movie_id,
    room_id_1,
    format_1,
    'SCHEDULED',
    CURRENT_DATE + start_clock_1 + (start_day_offset_1 || ' day')::INTERVAL,
            CURRENT_DATE + start_clock_1 + (start_day_offset_1 || ' day')::INTERVAL
        + (duration_minutes || ' minutes')::INTERVAL,
    base_price_1
FROM generated_showtimes

UNION ALL

SELECT
    NOW(),
    NOW(),
    movie_id,
    room_id_2,
    format_2,
    'SCHEDULED',
    CURRENT_DATE + start_clock_2 + (start_day_offset_2 || ' day')::INTERVAL,
            CURRENT_DATE + start_clock_2 + (start_day_offset_2 || ' day')::INTERVAL
        + (duration_minutes || ' minutes')::INTERVAL,
    base_price_2
FROM generated_showtimes;