package com.example.telegramschedule.model.handler;

import com.example.telegramschedule.DAO.ExcelReader;
import com.example.telegramschedule.model.BotState;
import com.example.telegramschedule.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
//processes incoming text message
public class MessageHandler {

    private final ExcelReader reader;
    private final MenuService menuService;

    public MessageHandler(ExcelReader reader, MenuService menuService) {
        this.reader = reader;
        this.menuService = menuService;
    }

    public SendMessage handle(Long chatId, BotState botState, String group) {
        switch (botState.name()) {
            case ("FIRST_STEP"):
                return menuService.firstStep(chatId);
            case ("MONDAY"):
                return menuService.getDayScheduleKeyboard(chatId, reader.getMonday(group));
            case ("TUESDAY"):
                return menuService.getDayScheduleKeyboard(chatId, reader.getTuesday(group));
            case ("WEDNESDAY"):
                return menuService.getDayScheduleKeyboard(chatId, reader.getWednesday(group));
            case ("THURSDAY"):
                return menuService.getDayScheduleKeyboard(chatId, reader.getThursday(group));
            case ("FRIDAY"):
                return menuService.getDayScheduleKeyboard(chatId, reader.getFriday(group));
            case ("SECOND_STEP"):
                return menuService.secondStep(chatId);
            case ("DAYSCHEDULE"):
                return menuService.getDayScheduleKeyboard(chatId, "");
            case ("THIRD_STEP"):
                return menuService.thirdStep(chatId);
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
