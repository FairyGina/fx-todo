package com.example.fx_todo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils
{
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return date.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM");
        return date.format(formatter);
    } //월간 달력 텍스트 포맷 설정

    public static String monthYearWeekFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM W번째 주");
        return date.format(formatter);
    } //주간 달력 텍스트 포맷 설정

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth(); //그 달의 마지막 날짜 저장

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1); //그 달의 첫번째 날 저장
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); //첫번째 날의 요일 저장(요일 숫자로 저장됨)

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null); //달력을 벗어난 곳은 채우지 않기
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek)); //달력의 날짜 채우기
        }
        return  daysInMonthArray;
    } //월간 달력 날짜 출력 함수

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        //LocalDate current = sundayForDate(selectedDate); //일요일 날짜 저장?
        LocalDate current = mondayForDate(selectedDate); //월요일 날짜 저장?
        LocalDate endDate = current.plusWeeks(1); //주의 마지막 날이면 다음 주로 넘기기

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1); //현재 주에서 전 주로 넘기기
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) //월요일 날짜 가져오는 함수
    {
        LocalDate oneWeekAgo = current.minusWeeks(1); //한 주 전이면 현재 주에서 빼기

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY) //월요일 날짜 받아서 비교하기
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    // private static LocalDate sundayForDate(LocalDate current)
    //{
    // LocalDate oneWeekAgo = current.minusWeeks(1);

    //while (current.isAfter(oneWeekAgo))
    //{
    //     if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
    //return current;

    // current = current.minusDays(1);
    // }

    //return null;
    // } 일요일 날짜 받아오는 함수
}
