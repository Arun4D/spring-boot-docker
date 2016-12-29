package com.springboot.study.controller;

import com.springboot.study.dto.HelloWorldDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by aduraisamy on 12/29/2016.
 */
@RestController
public class HelloworldController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/helloworld")
    public HelloWorldDTO welcome(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new HelloWorldDTO(counter.incrementAndGet(),
                String.format(template, name));
    }
}
