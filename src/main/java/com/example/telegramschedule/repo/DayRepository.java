package com.example.telegramschedule.repo;

import com.example.telegramschedule.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {
    @Override
    List<Day> findAll();

    @Override
    Optional<Day> findById(Long aLong);

    List<Day> findAllByDayName(String dayName);

    Day findByDayNameAndGroupName(String dayName, String group_name);

}
