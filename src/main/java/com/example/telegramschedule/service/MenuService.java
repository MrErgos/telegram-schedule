package com.example.telegramschedule.service;

import com.example.telegramschedule.DAO.DayDAO;
import com.example.telegramschedule.DAO.UserDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class MenuService {

    private DayDAO reader;
    private UserDAO userDAO;

    public MenuService(UserDAO dayDAO) {
        this.userDAO = dayDAO;
    }

    public SendMessage firstStep(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton("Да, нужно отправлять каждый день"));
        row2.add(new KeyboardButton("Нет, не нужно отправлять каждый день"));
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage request = SendMessage.builder()
                .chatId(chatId)
                .text("Вам нужно напоминать какие занятия будут завтра?")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        return request;
    }

    public SendMessage getDayScheduleKeyboard(Long chatId, String text) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Понедельник"));
        row1.add(new KeyboardButton("Вторник"));
        row2.add(new KeyboardButton("Среда"));
        row2.add(new KeyboardButton("Четверг"));
        row2.add(new KeyboardButton("Пятница"));
        row3.add(new KeyboardButton("Хочу изменить настройки"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage request = SendMessage.builder()
                .chatId(chatId)
                .text(text + "\nКакой день вас интересует?")
                .parseMode(ParseMode.HTML)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        return request;
    }

    public SendMessage secondStep(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add(new KeyboardButton("Да, нужно отправлять каждую пару"));
        row2.add(new KeyboardButton("Нет, не нужно отправлять каждую пару"));
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage request = SendMessage.builder()
                .chatId(chatId)
                .text("Вам нужно напоминать о паре за 30 минут?")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        return request;
    }

    public SendMessage thirdStep(Long chatId) {
        SendMessage request = SendMessage.builder().chatId(chatId)
                .text("Какая у вас группа, напишите ответ в виде \"Д-Э 215\"")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
        .build();

        return request;
    }

    public SendMessage getSettings(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(new KeyboardButton("Изменить алярм каждый день"));
        row2.add(new KeyboardButton("Изменить алярм каждое занятие"));
        row3.add(new KeyboardButton("Изменить группу"));
        row4.add(new KeyboardButton('\u2190' + "Назад"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboard);
        SendMessage request = SendMessage.builder()
                .text("Вот и настройки")
                .parseMode(ParseMode.HTML)
                .chatId(chatId)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        return request;
    }


}
