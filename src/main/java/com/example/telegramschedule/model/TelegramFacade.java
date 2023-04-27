package com.example.telegramschedule.model;

import com.example.telegramschedule.DAO.UserDAO;
import com.example.telegramschedule.entity.User;
import com.example.telegramschedule.model.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

    final MessageHandler messageHandler;

    UserDAO userDAO;

    public TelegramFacade(MessageHandler messageHandler, UserDAO userDAO) {
        this.messageHandler = messageHandler;
        this.userDAO = userDAO;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        Message message = update.getMessage();
        BotState botState;
        User user = userDAO.getOne(message.getChatId());
        switch (message.getText()) {
            case "/start": {
                if (!userDAO.isExist(message.getChatId())) {
                    User newUser = new User();
                    newUser.setId(message.getChatId());
                    userDAO.save(newUser);
                }
                return messageHandler.handle(message.getChatId(), BotState.THIRD_STEP, "");
            }
            case "Хочу изменить настройки": {
                botState = BotState.SETTINGS;
                break;
            }
            case "Изменить группу": {
                botState = BotState.THIRD_STEP;
                break;
            }
            case "Изменить алярм каждый день": {
                botState = BotState.FIRST_STEP;
                break;
            }
            case "Изменить алярм каждое занятие": {
                botState = BotState.SECOND_STEP;
                break;
            }
            case "Да, нужно отправлять каждый день": {
                user.setAlarmEveryDay(true);
                userDAO.save(user);
                botState = BotState.DAYSCHEDULE;
                break;
            }
            case "Нет, не нужно отправлять каждый день": {
                user.setAlarmEveryDay(false);
                userDAO.save(user);
                botState = BotState.DAYSCHEDULE;
                break;
            }
            case "Да, нужно отправлять каждую пару": {
                user.setAlarmEveryClass(true);
                userDAO.save(user);
                botState = BotState.DAYSCHEDULE;
                break;
            }
            case "Нет, не нужно отправлять каждую пару": {
                user.setAlarmEveryClass(false);
                userDAO.save(user);
                botState = BotState.DAYSCHEDULE;
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
            case '\u2190' + "Назад": {
                botState = BotState.DAYSCHEDULE;
                break;
            }
            default: {
                if (message.getText().matches("[а-яА-Я]-[а-яА-Я] \\d{3}")) {
                    user.setGroup(message.getText());
                    userDAO.save(user);
                    botState = BotState.DAYSCHEDULE;
                }
                else {
                    botState = BotState.FIRST_STEP;
                }
                break;
            }
        }
        if (user.getGroup() == null || user.getGroup().isEmpty()) {
            return messageHandler.handle(message.getChatId(), BotState.THIRD_STEP, "");
        }
        return messageHandler.handle(message.getChatId(), botState, user.getGroup());

    }
}
