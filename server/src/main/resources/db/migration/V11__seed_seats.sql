INSERT INTO seats (
    created_at,
    updated_at,
    room_id,
    seat_row,
    seat_number,
    seat_type,
    is_active
)
SELECT
    NOW(),
    NOW(),
    r.room_id,
    s.seat_row,
    s.seat_number,
    s.seat_type,
    TRUE
FROM
    (
        SELECT 1 AS room_id UNION ALL
        SELECT 2 UNION ALL
        SELECT 3 UNION ALL
        SELECT 4 UNION ALL
        SELECT 5 UNION ALL
        SELECT 6 UNION ALL
        SELECT 7 UNION ALL
        SELECT 8
    ) r
        CROSS JOIN
    (
        -- Row A: STANDARD
        SELECT 'A' AS seat_row, 1 AS seat_number, 'STANDARD' AS seat_type UNION ALL
        SELECT 'A', 2, 'STANDARD' UNION ALL
        SELECT 'A', 3, 'STANDARD' UNION ALL
        SELECT 'A', 4, 'STANDARD' UNION ALL
        SELECT 'A', 5, 'STANDARD' UNION ALL
        SELECT 'A', 6, 'STANDARD' UNION ALL

        -- Row B: STANDARD
        SELECT 'B', 1, 'STANDARD' UNION ALL
        SELECT 'B', 2, 'STANDARD' UNION ALL
        SELECT 'B', 3, 'STANDARD' UNION ALL
        SELECT 'B', 4, 'STANDARD' UNION ALL
        SELECT 'B', 5, 'STANDARD' UNION ALL
        SELECT 'B', 6, 'STANDARD' UNION ALL

        -- Row C: VIP
        SELECT 'C', 1, 'VIP' UNION ALL
        SELECT 'C', 2, 'VIP' UNION ALL
        SELECT 'C', 3, 'VIP' UNION ALL
        SELECT 'C', 4, 'VIP' UNION ALL
        SELECT 'C', 5, 'VIP' UNION ALL
        SELECT 'C', 6, 'VIP' UNION ALL

        -- Row D: COUPLE
        SELECT 'D', 1, 'COUPLE' UNION ALL
        SELECT 'D', 2, 'COUPLE' UNION ALL
        SELECT 'D', 3, 'COUPLE' UNION ALL
        SELECT 'D', 4, 'COUPLE'
    ) s
        ON CONFLICT (room_id, seat_row, seat_number) DO NOTHING;