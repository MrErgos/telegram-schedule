package com.example.telegramschedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long id;
    private boolean isAlarmEveryClass;
    private boolean isAlarmEveryDay;
    private String group;
}
