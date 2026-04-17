INSERT INTO cinemas (
    id,
    created_at,
    updated_at,
    name,
    address_line,
    city,
    state,
    postal_code,
    country,
    is_active
)
VALUES
    (1, NOW(), NOW(), 'Downtown Cinema', '101 Main Street', 'Morris', 'MN', '56267', 'USA', TRUE),
    (2, NOW(), NOW(), 'West Side Cinema', '250 West Center Ave', 'Morris', 'MN', '56267', 'USA', TRUE),
    (3, NOW(), NOW(), 'Northgate Cinema', '88 Northgate Blvd', 'Alexandria', 'MN', '56308', 'USA', TRUE),
    (4, NOW(), NOW(), 'Lakeside Cinema', '500 Lake Drive', 'Willmar', 'MN', '56201', 'USA', TRUE)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO rooms (
    id,
    created_at,
    updated_at,
    cinema_id,
    name,
    room_type,
    capacity,
    is_active
)
VALUES
    -- Cinema 1
    (1, NOW(), NOW(), 1, 'Room 1', 'STANDARD', 120, TRUE),
    (2, NOW(), NOW(), 1, 'Room 2', 'STANDARD', 100, TRUE),
    (3, NOW(), NOW(), 1, 'VIP Hall 1', 'VIP', 60, TRUE),

    -- Cinema 2
    (4, NOW(), NOW(), 2, 'Room 1', 'STANDARD', 110, TRUE),
    (5, NOW(), NOW(), 2, 'Room 2', 'STANDARD', 95, TRUE),
    (6, NOW(), NOW(), 2, 'IMAX Hall', 'IMAX', 180, TRUE),

    -- Cinema 3
    (7, NOW(), NOW(), 3, 'Room 1', 'STANDARD', 105, TRUE),
    (8, NOW(), NOW(), 3, 'VIP Hall 1', 'VIP', 70, TRUE),

    -- Cinema 4
    (9, NOW(), NOW(), 4, 'Room 1', 'STANDARD', 130, TRUE),
    (10, NOW(), NOW(), 4, '4DX Hall', 'FOUR_DX', 90, TRUE)
    ON CONFLICT (id) DO NOTHING;