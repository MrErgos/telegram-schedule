package com.example.telegramschedule.service;

import com.example.telegramschedule.config.ApplicationContextProvider;
import com.example.telegramschedule.model.ScheduleBot;
import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Setter
//thread with event
public class SendEvent extends Thread {

    private SendMessage sendMessage;

    public SendEvent() {
    }

    @SneakyThrows
    @Override
    public void run() {
        ScheduleBot telegramBot = ApplicationContextProvider.getApplicationContext().getBean(ScheduleBot.class);
        telegramBot.execute(sendMessage);
    }
}
