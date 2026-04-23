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
    (1, NOW(), NOW(), 'Silver Oak Cinema', '1458 Maple Hollow Drive', 'Brookdale', 'MN', '55111', 'USA', TRUE),
    (2, NOW(), NOW(), 'Moonlight Ridge Cinema', '7821 Cedar Lantern Avenue', 'Riverton', 'MN', '55112', 'USA', TRUE)
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
    (1, NOW(), NOW(), 1, 'Standard Hall 1', 'STANDARD', 120, TRUE),
    (2, NOW(), NOW(), 1, 'VIP Hall 1', 'VIP', 72, TRUE),
    (3, NOW(), NOW(), 1, 'IMAX Hall 1', 'IMAX', 180, TRUE),
    (4, NOW(), NOW(), 1, '4DX Hall 1', 'FOUR_DX', 96, TRUE),

    -- Cinema 2
    (5, NOW(), NOW(), 2, 'Standard Hall 1', 'STANDARD', 110, TRUE),
    (6, NOW(), NOW(), 2, 'VIP Hall 1', 'VIP', 68, TRUE),
    (7, NOW(), NOW(), 2, 'IMAX Hall 1', 'IMAX', 170, TRUE),
    (8, NOW(), NOW(), 2, '4DX Hall 1', 'FOUR_DX', 92, TRUE)
    ON CONFLICT (id) DO NOTHING;