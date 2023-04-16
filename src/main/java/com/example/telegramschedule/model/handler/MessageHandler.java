package com.example.telegramschedule.model.handler;

import com.example.telegramschedule.DAO.DayDAO;
import com.example.telegramschedule.model.BotState;
import com.example.telegramschedule.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
//processes incoming text message
public class MessageHandler {

    private final DayDAO dayDAO;
    private final MenuService menuService;

    public MessageHandler(DayDAO dayDAO, MenuService menuService) {
        this.dayDAO = dayDAO;
        this.menuService = menuService;
    }

    public SendMessage handle(Long chatId, BotState botState, String group) {
        if (group == null || group.isEmpty() || !group.matches("[а-яА-Я]-[а-яА-Я] \\d{3}")) {
            return menuService.thirdStep(chatId);
        }
        switch (botState.name()) {
            case ("FIRST_STEP"):
                return menuService.firstStep(chatId);
            case ("MONDAY"):
                return menuService.getDayScheduleKeyboard(chatId, dayDAO.getMonday(group));
            case ("TUESDAY"):
                return menuService.getDayScheduleKeyboard(chatId, dayDAO.getTuesday(group));
            case ("WEDNESDAY"):
                return menuService.getDayScheduleKeyboard(chatId, dayDAO.getWednesday(group));
            case ("THURSDAY"):
                return menuService.getDayScheduleKeyboard(chatId, dayDAO.getThursday(group));
            case ("FRIDAY"):
                return menuService.getDayScheduleKeyboard(chatId, dayDAO.getFriday(group));
            case ("SECOND_STEP"):
                return menuService.secondStep(chatId);
            case ("DAYSCHEDULE"):
                return menuService.getDayScheduleKeyboard(chatId, "");
            case ("THIRD_STEP"):
                return menuService.thirdStep(chatId);
            case ("SETTINGS"):
                return menuService.getSettings(chatId);
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
