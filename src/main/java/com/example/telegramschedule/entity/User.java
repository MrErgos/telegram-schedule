package com.example.telegramschedule.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "is_alarm_every_class")
    private boolean isAlarmEveryClass;

    @Column(name = "is_alarm_every_day")
    private boolean isAlarmEveryDay;

    @Column(name = "group_name")
    private String group;

    public User() {
    }

}
