package com.example.telegramschedule.service;

import com.example.telegramschedule.DAO.DayDAO;
import com.example.telegramschedule.DAO.UserDAO;
import com.example.telegramschedule.entity.Day;
import com.example.telegramschedule.entity.User;
import com.example.telegramschedule.model.BotState;
import com.example.telegramschedule.model.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@EnableScheduling
@Service
public class EventService {
    private final DayDAO dayDAO;
    private final UserDAO userDAO;

    @Autowired
    public EventService(DayDAO dayDAO, UserDAO userDAO) {
        this.dayDAO = dayDAO;
        this.userDAO = userDAO;
    }

    //start service in 8:30 MON-FRI cron = "35 15 * * * MON-FRI"
    @Scheduled(cron = "0 30 5 * * MON-FRI")
    private void eventService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;

        List<User> users = userDAO.findAllUsers();

        users.removeIf(user -> !user.isAlarmEveryDay());

        MessageHandler messageHandler = new MessageHandler(dayDAO, new MenuService(userDAO));

        for (User user : users) {
            BotState botState = BotState.DAYSCHEDULE;
            Long userId = user.getId();
            //create a thread for the upcoming event with the launch at a specific time
            SendEvent sendEvent = new SendEvent();
            switch (day) {
                case 1: {
                    botState = BotState.MONDAY;
                    break;
                }
                case 2: {
                    botState = BotState.TUESDAY;
                    break;
                }
                case 3: {
                    botState = BotState.WEDNESDAY;
                    break;
                }
                case 4: {
                    botState = BotState.THURSDAY;
                    break;
                }
                case 5: {
                    botState = BotState.FRIDAY;
                    break;
                }
            }
            sendEvent.setSendMessage(messageHandler.handle(userId, botState, user.getGroup()));

            new Timer().schedule(new SimpleTask(sendEvent), calendar.getTime());
        }
    }

    @Scheduled(cron = "* 30 5 * * MON-FRI")
    private void firstClass() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;

        List<User> users = userDAO.findAllUsers();
        users.removeIf(user -> !user.isAlarmEveryClass());
        users.removeIf(User::isAlarmEveryDay);

        MenuService menuService = new MenuService(userDAO);

        for (User user : users) {
            Day today = dayDAO.findDayByDayNameAndGroup(String.valueOf(day), user.getGroup());
            if (today.getCountOfClasses() != 0) {
                String classInfo = dayDAO.getClass(user.getGroup(), day, 1);
                SendEvent sendEvent = new SendEvent();
                SendMessage message;
                Long userId = user.getId();
                message = menuService.getDayScheduleKeyboard(userId, classInfo != null ? classInfo : "Поздравляю сейчас окно.");
                sendEvent.setSendMessage(message);

                new Timer().schedule(new SimpleTask(sendEvent), calendar.getTime());
            }
            else {
                SendEvent sendEvent = new SendEvent();
                SendMessage message;
                Long userId = user.getId();
                message = menuService.getDayScheduleKeyboard(userId, today.toString());
                sendEvent.setSendMessage(message);

                new Timer().schedule(new SimpleTask(sendEvent), calendar.getTime());
            }
        }
    }

    @Scheduled(cron = "* 30 7 * * MON-FRI")
    private void secondClass() {
        classAlarm(2);
    }

    @Scheduled(cron = "* 30 9 * * MON-FRI")
    private void thirdClass() {
        classAlarm(3);
    }

    @Scheduled(cron = "* 30 11 * * MON-FRI")
    private void forthClass() {
        classAlarm(4);
    }

    @Scheduled(cron = "* 30 13 * * MON-FRI")
    private void fifthClass() {
        classAlarm(5);
    }

    private void classAlarm(int classInDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;

        List<User> users = userDAO.findAllUsers();
        users.removeIf(user -> !user.isAlarmEveryClass());

        MenuService menuService = new MenuService(userDAO);

        for (User user : users) {
            if (dayDAO.findDayByDayNameAndGroup(String.valueOf(day), user.getGroup()).getCountOfClasses() != 0) {
                String classInfo = dayDAO.getClass(user.getGroup(), day, classInDay);
                SendEvent sendEvent = new SendEvent();
                SendMessage message;
                Long userId = user.getId();
                message = menuService.getDayScheduleKeyboard(userId, classInfo != null ? classInfo : "Поздравляю сейчас окно.");
                sendEvent.setSendMessage(message);

                new Timer().schedule(new SimpleTask(sendEvent), calendar.getTime());
            }
        }
    }
}
