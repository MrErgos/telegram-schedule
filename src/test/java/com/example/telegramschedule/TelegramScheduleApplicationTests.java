package com.example.telegramschedule;

import com.example.telegramschedule.DAO.ExcelReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class TelegramScheduleApplicationTests {

    @Test
    void contextLoads() {
        assertThat(new ExcelReader("src/main/java/com/example/telegramschedule/schedule.xlsx")).isNotNull();
    }

}
