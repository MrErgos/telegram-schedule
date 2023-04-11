package com.example.telegramschedule;

import com.example.telegramschedule.model.ScheduleBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TelegramScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramScheduleApplication.class, args);
    }
}
