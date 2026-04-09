INSERT INTO movies (
       title,
       description,
       duration_minutes,
       language,
       director,
       cast_members,
       poster_url,
       banner_url,
       trailer_url,
       release_date,
       age_rating,
       status
) VALUES (
       'The Conjuring',
       'Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their farmhouse.',
       112,
       'English',
       'James Wan',
       'Vera Farmiga, Patrick Wilson, Lili Taylor, Ron Livingston, Mackenzie Foy',
       'https://image.tmdb.org/t/p/original/wVYREutTvI2tmxr6ujrHT704wGF.jpg',
       'https://example.com/banners/TheConjuring.jpg',
       'https://www.youtube.com/watch?v=k10ETZ41q5o&t=2s',
       '2013-07-19',
       'R',
       'NOW_SHOWING'
    ),

       (
       'Dune: Part Two',
        'Paul Atreides unites with Chani and the Fremen while seeking revenge against those who destroyed his family.',
        166,
        'English',
        'Denis Villeneuve',
        'Timothee Chalamet, Zendaya, Rebecca Ferguson',
        'https://example.com/posters/dune2.jpg',
        'https://example.com/banners/dune2.jpg',
        'https://example.com/trailers/dune2',
        '2024-03-01',
        'PG',
        'NOW_SHOWING'
    ),

       (
       'The Batman',
        'Batman ventures into Gotham''s underworld when a sadistic killer leaves behind a trail of cryptic clues.',
        176,
        'English',
        'Matt Reeves',
        'Robert Pattinson, Zoe Kravitz, Paul Dano',
        'https://example.com/posters/thebatman.jpg',
        'https://example.com/banners/thebatman.jpg',
        'https://example.com/trailers/thebatman',
        '2022-03-04',
        'R',
        'ENDED'
    );
