package com.three.recipingeventservicebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecipingEventServiceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipingEventServiceBeApplication.class, args);
    }

}
