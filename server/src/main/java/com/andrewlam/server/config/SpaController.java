package com.andrewlam.server.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/movies",
            "/movies/{movieId}",

            "/cinemas",
            "/cinemas/{cinemaId}",

            "/showtimes/{showtimeId}",

            "/sign-in",
            "/register",
            "/account",

            "/buy-tickets/{movieId}",

            "/checkout/showtimes/{showtimeId}",

            "/booking-confirmed/{bookingCode}"
    })
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}