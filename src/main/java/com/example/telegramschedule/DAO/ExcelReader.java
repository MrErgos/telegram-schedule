package com.example.telegramschedule.DAO;

import com.example.telegramschedule.entity.User;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelReader {
    @Getter
    private XSSFWorkbook excelBook;
    private String fileName;
    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    public ExcelReader(@Value("${excel.fileName}")String fileName) {
        try {
            excelBook = new XSSFWorkbook(new FileInputStream(fileName));
            this.fileName = fileName;
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private String getDay(String day, String group) {
        XSSFSheet sheet = excelBook.getSheet("Table 1");
        Iterator rowIter = sheet.rowIterator();
        StringBuilder resultMessage = new StringBuilder();
        String[][] classes = new String[5][3];
        classes[0][0] = "<b>Первая пара: 09.00 - 10.35</b>";
        classes[1][0] = "<b>Вторая пара: 10.55 - 12.30</b>";
        classes[2][0] = "<b>Третья пара: 13.00 - 14.35</b>";
        classes[3][0] = "<b>Четвертая пара: 14.55 - 16.30</b>";
        classes[4][0] = "<b>Пятая пара: 16.50 - 18.25</b>";
        XSSFRow row;
        int groupColumnNum = 0;
        while (rowIter.hasNext()) {
            row = (XSSFRow) rowIter.next();
            Iterator cellIter = row.cellIterator();
            cellIter.next();
            cellIter.next();
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                if (cell.getStringCellValue().equals(group)) {
                    groupColumnNum = cell.getColumnIndex();
                }
            }
            if (groupColumnNum != 0) {
                break;
            }
        }
        if (groupColumnNum != 0) {
            while (rowIter.hasNext()) {
                row = (XSSFRow) rowIter.next();
                if (row.getCell(0).getStringCellValue().equals(day)) {
                    int i = 0;
                    int j = 1;
                    if (!row.getCell(groupColumnNum).getStringCellValue().isEmpty()) {
                        classes[i][j] = row.getCell(groupColumnNum).getStringCellValue().replaceAll("\n", " ");
                        j++;
                    }
                    while (rowIter.hasNext()) {
                        row = (XSSFRow) rowIter.next();
                        if (!row.getCell(0).getStringCellValue().isEmpty()) {
                            break;
                        }
                        if (!row.getCell(1).getStringCellValue().isEmpty()) {
                            i++;
                            j = 1;
                        }
                        if (row.getCell(groupColumnNum).getStringCellValue().isEmpty()) {
                            j++;
                            continue;
                        }
                        classes[i][j] = row.getCell(groupColumnNum).getStringCellValue().replaceAll("\n", " ");
                        j++;
                    }
                    break;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if ((classes[i][1] != null && !classes[i][1].isEmpty()) || (classes[i][2] != null && !classes[i][2].isEmpty())) {
                for (int j = 0; j < 3; j++) {
                    if (classes[i][j] != null && !classes[i][j].isEmpty()) {
                        resultMessage.append(classes[i][j]);
                        resultMessage.append("\n");
                    }
                }
            }
        }
        if (resultMessage.toString().isEmpty()) {
            return "В этот день пар нет. Отдыхай!";
        }
        return resultMessage.toString();
    }

    public String getMonday(String group) {
        return getDay("П О Н Е Д Е Л Ь Н И К", group);
    }
    public String getTuesday(String group) {
        return getDay("В Т О Р Н И К", group);
    }
    public String getWednesday(String group) {
        return getDay("С Р Е Д А", group);
    }
    public String getThursday(String group) {
        return getDay("Ч Е Т В Е Р Г", group);
    }
    public String getFriday(String group) {
        return getDay("П Я Т Н И Ц А", group);
    }

    public void save() {
        try {
            excelBook.write(new FileOutputStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        XSSFSheet sheet = excelBook.getSheet("Users");
        Iterator rowIter = sheet.rowIterator();
        while (rowIter.hasNext()) {
            XSSFRow row = (XSSFRow) rowIter.next();
            Iterator cellIter = row.cellIterator();
            XSSFCell cell = (XSSFCell) cellIter.next();
            long userID = (long) cell.getNumericCellValue();
            boolean isAlarmEveryDay = ((XSSFCell) cellIter.next()).getBooleanCellValue();
            boolean isAlarmEveryClass = ((XSSFCell) cellIter.next()).getBooleanCellValue();
            String group = ((XSSFCell) cellIter.next()).getStringCellValue();
            User user = new User(userID, isAlarmEveryClass, isAlarmEveryDay, group);
            users.add(user);
        }
        return users;
    }

    public User getUser(long id) {
        XSSFSheet sheet = excelBook.getSheet("Users");
        Iterator rowIter = sheet.rowIterator();
        while (rowIter.hasNext()) {
            XSSFRow row = (XSSFRow) rowIter.next();
            Iterator cellIter = row.cellIterator();
            XSSFCell cell = (XSSFCell) cellIter.next();
            long userID = (long) cell.getNumericCellValue();
            if (userID == id) {
                boolean isAlarmEveryDay = ((XSSFCell) cellIter.next()).getBooleanCellValue();
                boolean isAlarmEveryClass = ((XSSFCell) cellIter.next()).getBooleanCellValue();
                String group = ((XSSFCell) cellIter.next()).getStringCellValue();
                User user = new User(userID, isAlarmEveryClass, isAlarmEveryDay, group);
                return user;
            }
        }
        return null;
    }

    public void editUser(User user) {
        XSSFSheet sheet = excelBook.getSheet("Users");
        Iterator rowIter = sheet.rowIterator();
        while (rowIter.hasNext()) {
            XSSFRow row = (XSSFRow) rowIter.next();
            Iterator cellIter = row.cellIterator();
            XSSFCell cell = (XSSFCell) cellIter.next();
            long userID = (long) cell.getNumericCellValue();
            if (userID == user.getId()) {
                ((XSSFCell) cellIter.next()).setCellValue(user.isAlarmEveryDay());
                ((XSSFCell) cellIter.next()).setCellValue(user.isAlarmEveryClass());
                ((XSSFCell) cellIter.next()).setCellValue(user.getGroup());
            }
        }
        save();
    }

    public void createUser(User user) {
        XSSFSheet sheet = excelBook.getSheet("Users");
        if (getUser((long) user.getId()) != null) {
            return;
        }
        XSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
        XSSFCell idCell = row.createCell(0);
        idCell.setCellValue(user.getId());
        XSSFCell isAlarmEveryDayCell = row.createCell(1);
        isAlarmEveryDayCell.setCellValue(user.isAlarmEveryDay());
        XSSFCell isAlarmEveryClassCell = row.createCell(2);
        isAlarmEveryClassCell.setCellValue(user.isAlarmEveryClass());
        XSSFCell groupCell = row.createCell(3);
        groupCell.setCellValue(user.getGroup());
        save();
    }
}
