package com.demo.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    record PingPong(String result) {}

    @GetMapping("/ping")
    public PingPong pingPong() {
        return new PingPong("pong pong :(");
    }

}
