package com.example.telegramschedule.DAO;

import com.example.telegramschedule.entity.Day;
import com.example.telegramschedule.helper.CSVHelper;
import com.example.telegramschedule.repo.DayRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DayDAO {
    private final DayRepository dayRepository;
    private List<Day> classes;

    @Autowired
    public DayDAO(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public List<Day> findAllDays() {
        return dayRepository.findAll();
    }

    public Day find(Long id) {
        return dayRepository.findById(id).get();
    }

    public List<Day> findDaysByDayName(String dayName) {
        return dayRepository.findAllByDayName(dayName);
    }

    public Day findDayByDayNameAndGroup(String dayName, String group_name) {
        return dayRepository.findByDayNameAndGroupName(dayName, group_name);
    }

    public void saveAll(MultipartFile file) {
        try {
            List<Day> days = CSVHelper.csvToTutorials(file.getInputStream());
            dayRepository.saveAll(days);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public String getClass(String group, int dayInWeek, int classInDay) {
        if (group == null || group.isEmpty()) {
            return "Возможно вы не указали свою группу.";
        }
        String resultMessage = "";
        switch (classInDay) {
            case 1: {
                resultMessage = "<b>Первая пара: 09.00 - 10.35</b> \n\n" +
                        "➤ " + findDayByDayNameAndGroup(String.valueOf(dayInWeek), group).getClass_1() + "\n";
                return resultMessage;
            }
            case 2: {
                resultMessage = "<b>Вторая пара: 10.55 - 12.30</b> \n\n" +
                        "➤ " + findDayByDayNameAndGroup(String.valueOf(dayInWeek), group).getClass_2() + "\n";
                return resultMessage;
            }
            case 3: {
                resultMessage = "<b>Третья пара: 13.00 - 14.35</b> \n\n" +
                        "➤ " + findDayByDayNameAndGroup(String.valueOf(dayInWeek), group).getClass_3() + "\n";
                return resultMessage;
            }
            case 4: {
                resultMessage = "<b>Четвертая пара: 14.55 - 16.30</b> \n\n" +
                        "➤ " + findDayByDayNameAndGroup(String.valueOf(dayInWeek), group).getClass_4() + "\n";
                return resultMessage;
            }
            case 5: {
                resultMessage = "<b>Пятая пара: 16.50 - 18.25</b> \n\n" +
                        "➤ " + findDayByDayNameAndGroup(String.valueOf(dayInWeek), group).getClass_5() + "\n";
                return resultMessage;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + group + " : " + dayInWeek + " : " + classInDay);
        }
    }

    public String getMonday(String group) {
        return group != null && !group.isEmpty() ? findDayByDayNameAndGroup("1", group).toString() : "Возможно вы не указали свою группу.";
    }
    public String getTuesday(String group) {
        return findDayByDayNameAndGroup("2", group).toString();
    }
    public String getWednesday(String group) {
        return findDayByDayNameAndGroup("3", group).toString();
    }
    public String getThursday(String group) {
        return findDayByDayNameAndGroup("4", group).toString();
    }
    public String getFriday(String group) {
        return findDayByDayNameAndGroup("5", group).toString();
    }
}
