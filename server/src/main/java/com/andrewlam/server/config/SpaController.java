package com.andrewlam.server.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(
            value = {"/", "/{*path}"},
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}