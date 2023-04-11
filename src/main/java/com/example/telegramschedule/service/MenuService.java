package com.example.telegramschedule.service;

import com.example.telegramschedule.DAO.ExcelReader;
import com.example.telegramschedule.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class MenuService {

    private ExcelReader reader;

    public MenuService(ExcelReader reader) {
        this.reader = reader;
    }

    public SendMessage firstStep(Long chatId) {
        reader.createUser(new User(chatId, false, false, ""));
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
                .text("Для начала я должен понять, что вам нужно от меня. " +
                "Вам нужно напоминать какие занятия будут завтра?")
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
        row1.add(new KeyboardButton("Понедельник"));
        row1.add(new KeyboardButton("Вторник"));
        row2.add(new KeyboardButton("Среда"));
        row2.add(new KeyboardButton("Четверг"));
        row2.add(new KeyboardButton("Пятница"));
        keyboard.add(row1);
        keyboard.add(row2);
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
}
