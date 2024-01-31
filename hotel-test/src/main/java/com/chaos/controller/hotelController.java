package com.chaos.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class hotelController {
    @GetMapping("/hotel")
    public String test() {
        System.out.println("111");
        return "111";
    }
}
