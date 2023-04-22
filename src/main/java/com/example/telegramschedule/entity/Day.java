package com.example.telegramschedule.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Day {
    @Setter
    @Id
    private Long id;

    @Setter
    @Column(name = "day_name")
    private String dayName;
    @Setter
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "class_1")
    private String class_1;

    @Column(name = "class_2")
    private String class_2;

    @Column(name = "class_3")
    private String class_3;

    @Column(name = "class_4")
    private String class_4;

    @Column(name = "class_5")
    private String class_5;

    public int getCountOfClasses() {
        int countOfClassTime = 0;
        if (class_1 != null && !class_1.isEmpty()) {
            countOfClassTime++;
        }
        if (class_2 != null && !class_2.isEmpty()) {
            countOfClassTime++;
        }
        if (class_3 != null && !class_3.isEmpty()) {
            countOfClassTime++;
        }
        if (class_4 != null && !class_4.isEmpty()) {
            countOfClassTime++;
        }
        if (class_5 != null && !class_5.isEmpty()) {
            countOfClassTime++;
        }
        return countOfClassTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (getCountOfClasses() == 0) {
            return "В этот день пар нет. Отдыхай!\n";
        }
        builder.append("<b>Первая пара: 09.00 - 10.35</b> \n" + "➤ " + getClass_1() + "\n\n");
        builder.append("<b>Вторая пара: 10.55 - 12.30</b>\n" + "➤ " + getClass_2() + "\n\n");
        builder.append("<b>Третья пара: 13.00 - 14.35</b>\n" + "➤ " + getClass_3() + "\n\n");
        builder.append("<b>Четвертая пара: 14.55 - 16.30</b>\n" + "➤ " + getClass_4() + "\n\n");
        builder.append("<b>Пятая пара: 16.50 - 18.25</b>\n" + "➤ " + getClass_5() + "\n");
        return builder.toString();
    }

    public Day() {
    }
}
