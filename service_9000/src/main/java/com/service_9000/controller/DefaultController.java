package com.service_9000.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String index() {
        return "<p><h1>HELLO WORLD</h1><p>";
    }

}
