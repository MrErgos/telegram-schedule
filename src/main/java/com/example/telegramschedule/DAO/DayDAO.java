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
