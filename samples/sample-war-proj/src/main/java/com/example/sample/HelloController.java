package com.example.sample;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(value = {"/ping", "/"})
    public String ping() {
        return "pong";
    }
}
