package com.example.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private BuildProperties buildProperties;

    @RequestMapping(value = {"/ping", "/"})
    public String ping() {
        return "pong : " + buildProperties.getVersion();
    }
}
