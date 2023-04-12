package com.example.telegramschedule.controller;

import com.example.telegramschedule.model.ScheduleBot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final ScheduleBot telegramBot;

    public WebhookController(ScheduleBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    // point for message
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/home")
    public ResponseEntity get() {
        return ResponseEntity.ok().build();
    }
}
