package com.example.telegramschedule.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.example.telegramschedule.entity.Day;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Day> csvToTutorials(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.RFC4180.withFirstRecordAsHeader().withIgnoreHeaderCase())) {

            List<Day> days = new ArrayList<Day>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Day day = new Day();
                day.setId(Long.parseLong(csvRecord.get("id")));
                day.setDayName(csvRecord.get("day_name"));
                day.setGroupName(csvRecord.get("group_name"));
                day.setClass_1(csvRecord.get("class_1"));
                day.setClass_2(csvRecord.get("class_2"));
                day.setClass_3(csvRecord.get("class_3"));
                day.setClass_4(csvRecord.get("class_4"));
                day.setClass_5(csvRecord.get("class_5"));
                days.add(day);
            }

            return days;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
