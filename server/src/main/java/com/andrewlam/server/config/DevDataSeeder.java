package com.andrewlam.server.config;

import com.andrewlam.server.enums.RoomType;
import com.andrewlam.server.enums.ShowtimeFormat;
import com.andrewlam.server.enums.ShowtimeStatus;
import com.andrewlam.server.model.Cinema;
import com.andrewlam.server.model.Movie;
import com.andrewlam.server.model.Room;
import com.andrewlam.server.model.Showtime;
import com.andrewlam.server.repository.CinemaRepository;
import com.andrewlam.server.repository.MovieRepository;
import com.andrewlam.server.repository.RoomRepository;
import com.andrewlam.server.repository.ShowtimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public DevDataSeeder(
            CinemaRepository cinemaRepository,
            RoomRepository roomRepository,
            ShowtimeRepository showtimeRepository,
            MovieRepository movieRepository
    ) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (cinemaRepository.count() > 0 || roomRepository.count() > 0 || showtimeRepository.count() > 0) {
            log.info("Dev seed skipped because cinema/room/showtime data already exists.");
            return;
        }

        Cinema downtownCinema = new Cinema();
        downtownCinema.setName("Downtown Cinema");
        downtownCinema.setAddressLine("101 Main Street");
        downtownCinema.setCity("Morris");
        downtownCinema.setState("MN");
        downtownCinema.setPostalCode("56267");
        downtownCinema.setCountry("USA");
        downtownCinema.setIsActive(true);

        Cinema westSideCinema = new Cinema();
        westSideCinema.setName("West Side Cinema");
        westSideCinema.setAddressLine("250 West Center Ave");
        westSideCinema.setCity("Morris");
        westSideCinema.setState("MN");
        westSideCinema.setPostalCode("56267");
        westSideCinema.setCountry("USA");
        westSideCinema.setIsActive(true);

        cinemaRepository.saveAll(List.of(downtownCinema, westSideCinema));

        Room room1 = new Room();
        room1.setCinema(downtownCinema);
        room1.setName("Room 1");
        room1.setRoomType(RoomType.STANDARD);
        room1.setCapacity(120);
        room1.setIsActive(true);

        Room room2 = new Room();
        room2.setCinema(downtownCinema);
        room2.setName("Room 2");
        room2.setRoomType(RoomType.STANDARD);
        room2.setCapacity(90);
        room2.setIsActive(true);

        Room room3 = new Room();
        room3.setCinema(westSideCinema);
        room3.setName("IMAX Hall");
        room3.setRoomType(RoomType.IMAX);
        room3.setCapacity(180);
        room3.setIsActive(true);

        Room room4 = new Room();
        room4.setCinema(westSideCinema);
        room4.setName("VIP Hall");
        room4.setRoomType(RoomType.VIP);
        room4.setCapacity(60);
        room4.setIsActive(true);

        roomRepository.saveAll(List.of(room1, room2, room3, room4));

        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            log.warn("No movies found. Seeded cinemas and rooms only. Add movie data first, then rerun to seed showtimes.");
            return;
        }

        Movie movie1 = movies.get(0);
        Movie movie2 = movies.size() > 1 ? movies.get(1) : movies.get(0);
        Movie movie3 = movies.size() > 2 ? movies.get(2) : movies.get(0);

        LocalDateTime baseTime = LocalDateTime.now()
                .plusDays(1)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        Showtime showtime1 = buildShowtime(
                movie1,
                room1,
                ShowtimeFormat.TWO_D,
                ShowtimeStatus.SCHEDULED,
                baseTime.withHour(13),
                baseTime.withHour(15).plusMinutes(15),
                new BigDecimal("12.99")
        );

        Showtime showtime2 = buildShowtime(
                movie2,
                room2,
                ShowtimeFormat.THREE_D,
                ShowtimeStatus.SCHEDULED,
                baseTime.withHour(16),
                baseTime.withHour(18).plusMinutes(20),
                new BigDecimal("14.99")
        );

        Showtime showtime3 = buildShowtime(
                movie1,
                room3,
                ShowtimeFormat.IMAX,
                ShowtimeStatus.SCHEDULED,
                baseTime.withHour(19),
                baseTime.withHour(21).plusMinutes(30),
                new BigDecimal("18.99")
        );

        Showtime showtime4 = buildShowtime(
                movie3,
                room4,
                ShowtimeFormat.FOUR_DX,
                ShowtimeStatus.SCHEDULED,
                baseTime.plusDays(1).withHour(14),
                baseTime.plusDays(1).withHour(16).plusMinutes(10),
                new BigDecimal("21.99")
        );

        showtimeRepository.saveAll(List.of(showtime1, showtime2, showtime3, showtime4));

        log.info("Dev seed completed: cinemas, rooms, and showtimes inserted.");
    }

    private Showtime buildShowtime(
            Movie movie,
            Room room,
            ShowtimeFormat format,
            ShowtimeStatus status,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal basePrice
    ) {
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setFormat(format);
        showtime.setStatus(status);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        showtime.setBasePrice(basePrice);
        return showtime;
    }
}