package com.deepshooter.birthdayapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import timber.log.Timber;

public class DateUtils {


    public static String convertDateInMonthFormat(String dateString) {

        SimpleDateFormat convertedFormat = new SimpleDateFormat("dd MMMM,yyyy");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(dateString);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateInDayFormat(String dateString) {

        SimpleDateFormat convertedFormat = new SimpleDateFormat("E, MMM dd");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(dateString);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFullBirthday(String dateString) {

        SimpleDateFormat convertedFormat = new SimpleDateFormat("dd MMMM yyyy (EEEE)");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(dateString);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String birthdayMonth(String dateString) {

        SimpleDateFormat convertedFormat = new SimpleDateFormat("MMMM");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(dateString);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDate() {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(currentDate);
    }


    public static String getZodiacSign(int day, String month) {
        String sign = "";
        if (month.equalsIgnoreCase("January")) {
            if (day < 20)
                sign = "Capricorn";
            else
                sign = "Aquarius";
        } else if (month.equalsIgnoreCase("February")) {
            if (day < 19)
                sign = "Aquarius";
            else
                sign = "Pisces";
        } else if (month.equalsIgnoreCase("March")) {
            if (day < 21)
                sign = "Pisces";
            else
                sign = "Aries";
        } else if (month.equalsIgnoreCase("April")) {
            if (day < 20)
                sign = "Aries";
            else
                sign = "Taurus";
        } else if (month.equalsIgnoreCase("May")) {
            if (day < 21)
                sign = "Taurus";
            else
                sign = "Gemini";
        } else if (month.equalsIgnoreCase("June")) {
            if (day < 21)
                sign = "Gemini";
            else
                sign = "Cancer";
        } else if (month.equalsIgnoreCase("July")) {
            if (day < 23)
                sign = "Cancer";
            else
                sign = "Leo";
        } else if (month.equalsIgnoreCase("August")) {
            if (day < 23)
                sign = "Leo";
            else
                sign = "Virgo";
        } else if (month.equalsIgnoreCase("September")) {
            if (day < 23)
                sign = "Virgo";
            else
                sign = "Libra";
        } else if (month.equalsIgnoreCase("October")) {
            if (day < 23)
                sign = "Libra";
            else
                sign = "Scorpio";
        } else if (month.equalsIgnoreCase("November")) {
            if (day < 22)
                sign = "scorpio";
            else
                sign = "Sagittarius";
        } else if (month.equalsIgnoreCase("December")) {
            if (day < 22)
                sign = "Sagittarius";
            else
                sign = "Capricorn";
        }

        return sign;
    }


    public static String getRemainingMonth(String birthDate) {
        String currentYearBirthDate = "";
        String currentDate = getCurrentDate();
        String currentYear = currentDate.substring(6, 10);
        String birthday = birthDate.substring(3, 5);
        String birthMonth = birthDate.substring(0, 2);

        currentYearBirthDate = birthMonth + "/" + birthday + "/" + currentYear;

        try {
            if (new SimpleDateFormat("MM/dd/yyyy").parse(currentYearBirthDate).after(new SimpleDateFormat("MM/dd/yyyy").parse(getCurrentDate()))) {
                currentYearBirthDate = birthMonth + "/" + birthday + "/" + currentYear;
            } else {
                int nextYear = Integer.parseInt(currentYear) + 1;
                currentYearBirthDate = birthMonth + "/" + birthday + "/" + nextYear;
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dateToday = simpleDateFormat.parse(getCurrentDate());
            Date dateComing = simpleDateFormat.parse(currentYearBirthDate);

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(dateToday);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(dateComing);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            return diffMonth + "";

        } catch (ParseException e) {
            e.printStackTrace();
            Timber.e("Okay 2 " + e.getMessage());
        }

        return null;
    }


    public static String getComingBirthday(String birthDate) {
        String currentDate = getCurrentDate();
        String currentYear = currentDate.substring(6, 10);
        String birthday = birthDate.substring(3, 5);
        String birthMonth = birthDate.substring(0, 2);
        String currentYearBirthDate = birthMonth + "/" + birthday + "/" + currentYear;

        SimpleDateFormat convertedFormat = new SimpleDateFormat("E, MMM dd");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(currentYearBirthDate);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getComingBirthdayFullFormat(String birthDate) {
        String currentDate = getCurrentDate();
        String currentYear = currentDate.substring(6, 10);
        String birthday = birthDate.substring(3, 5);
        String birthMonth = birthDate.substring(0, 2);
        String currentYearBirthDate = birthMonth + "/" + birthday + "/" + currentYear;

        SimpleDateFormat convertedFormat = new SimpleDateFormat("EEEE, MMMM dd");
        SimpleDateFormat actualFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = actualFormat.parse(currentYearBirthDate);
            String convertedDate = convertedFormat.format(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
