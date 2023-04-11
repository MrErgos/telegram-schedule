package com.example.telegramschedule.model;

import com.example.telegramschedule.DAO.ExcelReader;
import com.example.telegramschedule.entity.User;
import com.example.telegramschedule.model.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

    final MessageHandler messageHandler;

    ExcelReader reader;

    public TelegramFacade(MessageHandler messageHandler, ExcelReader reader) {
        this.messageHandler = messageHandler;
        this.reader = reader;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        Message message = update.getMessage();
        BotState botState;
        User user = reader.getUser(message.getChatId());
        switch (message.getText()) {
            case "/start": {
                botState = BotState.FIRST_STEP;
                break;
            }
            case "Да, нужно отправлять каждый день": {
                user.setAlarmEveryDay(true);
                reader.editUser(user);
                botState = BotState.SECOND_STEP;
                break;
            }
            case "Нет, не нужно отправлять каждый день": {
                user.setAlarmEveryDay(false);
                reader.editUser(user);
                botState = BotState.SECOND_STEP;
                break;
            }
            case "Да, нужно отправлять каждую пару": {
                user.setAlarmEveryClass(true);
                reader.editUser(user);
                botState = BotState.THIRD_STEP;
                break;
            }
            case "Нет, не нужно отправлять каждую пару": {
                user.setAlarmEveryClass(false);
                reader.editUser(user);
                botState = BotState.THIRD_STEP;
                break;
            }
            case "Понедельник": {
                botState = BotState.MONDAY;
                break;
            }
            case "Вторник": {
                botState = BotState.TUESDAY;
                break;
            }
            case "Среда": {
                botState = BotState.WEDNESDAY;
                break;
            }
            case "Четверг": {
                botState = BotState.THURSDAY;
                break;
            }
            case "Пятница": {
                botState = BotState.FRIDAY;
                break;
            }
            default: {
                if (message.getText().matches("[а-яА-Я]-[а-яА-Я] \\d{3}")) {
                    user.setGroup(message.getText());
                    reader.editUser(user);
                    botState = BotState.DAYSCHEDULE;
                }
                else {
                    botState = BotState.FIRST_STEP;
                }
                break;
            }
        }
        return messageHandler.handle(message.getChatId(), botState, user.getGroup());

    }
}
