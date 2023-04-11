package com.example.telegramschedule;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@NoArgsConstructor
public class ScheduleBot {
    private final TelegramBot bot = new TelegramBot("5874204988:AAGSrcOIsbM4P78RxZepAwyaOqSopIpACeg");
    ExcelReader reader = new ExcelReader("src/main/java/com/example/telegramschedule/1676644625_2 курс.xlsx");


    public void main() {
        SetWebhook request = new SetWebhook()
                .url("url")
                .certificate(new byte[]{}) // byte[]
                .certificate(new File("src/main/java/com/example/telegramschedule/Certigicate.txt"));
        BaseResponse response = bot.execute(request);
        bot.
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                for (Update update : updates) {
                    User user = reader.getUser(update.message().chat().id());
                    switch (update.message().text()) {
                        case "/start": {
                            createNewUser(update);
                            break;
                        }
                        case "Да, нужно отправлять каждый день": {
                            user.setAlarmEveryDay(true);
                            reader.editUser(user);
                            setAlarmEveryClass(update);
                            break;
                        }
                        case "Нет, не нужно отправлять каждый день": {
                            user.setAlarmEveryDay(false);
                            reader.editUser(user);
                            setAlarmEveryClass(update);
                            break;
                        }
                        case "Да, нужно отправлять каждую пару": {
                            user.setAlarmEveryClass(true);
                            reader.editUser(user);
                            whatsGroupMessage(update);
                            break;
                        }
                        case "Нет, не нужно отправлять каждую пару": {
                            user.setAlarmEveryClass(false);
                            reader.editUser(user);
                            whatsGroupMessage(update);
                            break;
                        }
                        case "Понедельник": {
                            getDayScheduleKeyboard(update, reader.getMonday(user.getGroup()));
                            break;
                        }

                        case "Вторник": {
                            getDayScheduleKeyboard(update, reader.getTuesday(user.getGroup()));
                            break;
                        }
                        case "Среда": {
                            getDayScheduleKeyboard(update, reader.getWednesday(user.getGroup()));
                            break;
                        }
                        case "Четверг": {
                            getDayScheduleKeyboard(update, reader.getThursday(user.getGroup()));
                            break;
                        }
                        case "Пятница": {
                            getDayScheduleKeyboard(update, reader.getFriday(user.getGroup()));
                            break;
                        }
                        default: {
                            if (update.message().text().matches("[а-яА-Я]-[а-яА-Я] \\d{3}")) {
                                user.setGroup(update.message().text());
                                reader.editUser(user);
                                getDayScheduleKeyboard(update, "");
                            }
                            break;
                        }
                    }
                }

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    public void createNewUser(Update update) {
        reader.createUser(new User(update.message().chat().id(), false, false, ""));
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Да, нужно отправлять каждый день"},
                new String[]{"Нет, не нужно отправлять каждый день"})
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);
        SendMessage request = new SendMessage(update.message().chat().id(), "Для начала я должен понять, что вам нужно от меня. " +
                "Вам нужно напоминать какие занятия будут завтра?")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

    private void getDayScheduleKeyboard(Update update, String text) {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Понедельник", "Вторник"},
                new String[]{"Среда", "Четверг", "Пятница"})
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);
        SendMessage request = new SendMessage(update.message().chat().id(), text + "\nКакой день вас интересует?")
                .parseMode(ParseMode.HTML)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

    private void setAlarmEveryClass(Update update) {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Да, нужно отправлять каждую пару"},
                new String[]{"Нет, не нужно отправлять каждую пару"})
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);
        SendMessage request = new SendMessage(update.message().chat().id(), "Вам нужно напоминать о паре за 30 минут?")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }

    private void whatsGroupMessage(Update update) {
        SendMessage request = new SendMessage(update.message().chat().id(), "Какая у вас группа, напишите ответ в виде \"Д-Э 215\"")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(new ForceReply());
        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
    }
}
