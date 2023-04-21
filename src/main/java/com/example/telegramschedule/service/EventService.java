package com.example.telegramschedule.service;

import com.example.telegramschedule.DAO.DayDAO;
import com.example.telegramschedule.DAO.UserDAO;
import com.example.telegramschedule.entity.User;
import com.example.telegramschedule.model.BotState;
import com.example.telegramschedule.model.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    @Scheduled(cron = "* 30 8 * * MON-FRI")
    private void eventService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        List<User> users = userDAO.findAllUsers();

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
}
