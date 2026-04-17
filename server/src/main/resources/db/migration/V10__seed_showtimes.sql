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
) VALUES
      (
          NOW(),
          NOW(),
          1,
          1,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '13:00:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '14:52:00' + INTERVAL '1 day',
          12.99
      ),
      (
          NOW(),
          NOW(),
          2,
          2,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '15:30:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '18:16:00' + INTERVAL '1 day',
          13.99
      ),
      (
          NOW(),
          NOW(),
          3,
          3,
          'THREE_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '18:00:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '20:56:00' + INTERVAL '1 day',
          15.99
      ),
      (
          NOW(),
          NOW(),
          4,
          4,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '12:00:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '14:49:00' + INTERVAL '1 day',
          12.49
      ),
      (
          NOW(),
          NOW(),
          5,
          5,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '20:00:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '23:01:00' + INTERVAL '1 day',
          14.99
      ),
      (
          NOW(),
          NOW(),
          6,
          6,
          'IMAX',
          'SCHEDULED',
          CURRENT_DATE + TIME '19:00:00' + INTERVAL '1 day',
          CURRENT_DATE + TIME '21:28:00' + INTERVAL '1 day',
          18.99
      ),
      (
          NOW(),
          NOW(),
          7,
          7,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '14:30:00' + INTERVAL '2 day',
          CURRENT_DATE + TIME '16:58:00' + INTERVAL '2 day',
          13.49
      ),
      (
          NOW(),
          NOW(),
          8,
          8,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '17:00:00' + INTERVAL '2 day',
          CURRENT_DATE + TIME '20:00:00' + INTERVAL '2 day',
          20.99
      ),
      (
          NOW(),
          NOW(),
          9,
          9,
          'TWO_D',
          'SCHEDULED',
          CURRENT_DATE + TIME '16:15:00' + INTERVAL '2 day',
          CURRENT_DATE + TIME '18:27:00' + INTERVAL '2 day',
          11.99
      ),
      (
          NOW(),
          NOW(),
          10,
          10,
          'FOUR_DX',
          'SCHEDULED',
          CURRENT_DATE + TIME '20:30:00' + INTERVAL '2 day',
          CURRENT_DATE + TIME '22:02:00' + INTERVAL '2 day',
          21.99
      );