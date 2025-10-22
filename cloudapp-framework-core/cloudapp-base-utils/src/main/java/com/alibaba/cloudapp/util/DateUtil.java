
/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.util;

import java.text.DateFormatSymbols;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    /**
     * default timestamp format:{@value}
     */
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * default time format:{@value}
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * default data format:{@value}
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * default data format:{@value}
     */
    public static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";
    /**
     * default data format:{@value}
     */
    public static final String DATE_FORMAT_YEAR_MONTH_DAY = "yyyyMMdd";

    private static final List<Character> ORACLE_FORMAT_SPLIT = Arrays.asList(
            '-', '/', ' ', ':', '.', '年', '月', '日', '时', '分', '秒', ' '
    );

    /**
     * Convert the date string in the format yyyy-MM-dd format to the format yyyyMMdd
     *
     * @param dateString Date string in the format yyyy-MM-dd
     * @return Date string in the format yyyyMMdd
     */
    public static String reformatDateWithoutCenterLine(String dateString) {
        if (dateString == null) {
            return "";
        }

        return dateString.replaceAll("-", "");
    }

    /**
     * Calculate the difference in months between two dates
     * (year-month type, format is yyyyMM | yyyyMMdd)
     *
     * @param startDate - Start date
     * @param endDate   - End date
     * @return the difference of the months
     */
    public static int calculateDifferenceMonth(final String startDate, final String endDate) {
        int length;
        if (startDate.length() != 6 && startDate.length() != 8) {
            throw new IllegalArgumentException("start date is not illegal, " +
                    "Only accept dates in the format 'yyyyMM' | 'yyyyMMdd'");
        }

        if (endDate.length() != 6 && endDate.length() != 8) {
            throw new IllegalArgumentException("end date is not illegal, Only accept"
                    + " dates in the format 'yyyyMM' | 'yyyyMMdd'");
        }

        final int dealYearInt = Integer.parseInt(startDate.substring(0, 4));
        final int dealMonthInt = Integer.parseInt(startDate.substring(4, 6));
        final int alterYearInt = Integer.parseInt(endDate.substring(0, 4));
        final int alterMonthInt = Integer.parseInt(endDate.substring(4, 6));
        return (dealYearInt - alterYearInt) * 12 + (dealMonthInt - alterMonthInt);
    }

    /**
     * Get the year of date
     *
     * @param date java.util.date date
     * @return the year of date
     */
    public static int getYearOfDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get the string consisting of year and month in the date
     *
     * @param d java.util.date date
     * @return Year and month string in yyyyMM format
     */
    public static String formatDateToYYYYMM(final Date d) {
        final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH, new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * Get the string consisting of year, month and day in the date
     *
     * @param d java.util.date
     * @return A string in the format yyyyMMdd
     */
    public static String formatDateToYYYYMMDD(final Date d) {
        final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY, new DateFormatSymbols());
        return df.format(d);
    }

    /**
     * Get the date string in the input format. The string follows the Oracle format.
     *
     * @param d      - date
     * @param format - Specify the date format. The format is written in Oracle format.
     * @return The date string converted according to the specified date format
     */
    public static String formatDate(final Date d, final String format) {
        if (d == null) {
            return "";
        }

        final SimpleDateFormat df = new SimpleDateFormat(format, new DateFormatSymbols());

        return df.format(d);
    }

    /**
     * Get the number of days between two dates
     *
     * @param after  the bigger date
     * @param before the smaller data
     * @return the number of days between two dates
     */
    public static int calculateDifferenceDay(final Date after, final Date before) {
        int days = 0;
        final Calendar calo = Calendar.getInstance();
        final Calendar caln = Calendar.getInstance();
        calo.setTime(before);
        caln.setTime(after);

        final int oday = calo.get(Calendar.DAY_OF_YEAR);
        final int nyear = caln.get(Calendar.YEAR);
        int oyear = calo.get(Calendar.YEAR);

        while (nyear > oyear) {
            calo.set(Calendar.MONTH, 11);
            calo.set(Calendar.DATE, 31);
            days = days + calo.get(Calendar.DAY_OF_YEAR);
            oyear = oyear + 1;
            calo.set(Calendar.YEAR, oyear);
        }

        final int nday = caln.get(Calendar.DAY_OF_YEAR);
        days = days + nday - oday;

        return days;
    }

    /**
     * Get previous month date
     *
     * @param yearMonth date, format yyyyMM
     * @return date
     */
    public static String getPreviousMonthDate(final String yearMonth) {
        final int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4, 6));

        month = month - 1;

        if (month >= 10) {
            return yearMonth.substring(0, 4) + (Integer.valueOf(month)).toString();
        } else if (month > 0) {
            return yearMonth.substring(0, 4) + "0" + (Integer.valueOf(month)).toString();
        } else {
            // if(month>12)
            return (Integer.valueOf(year - 1)).toString() + (Integer.valueOf(month + 12)).toString();
        }
    }

    /**
     * get the year month string after months
     *
     * @param date  String
     * @param after int
     * @return String
     */
    public static String getDateAfterMonth(final String date, final int after) {

        int year = Integer.parseInt(date) / 100;
        int month = Integer.parseInt(date) % 100;

        final int addYear = after / 12;
        final int addMonth = after % 12;

        year = year + addYear;
        month = month + addMonth;

        if (month > 12) {
            year = year + 1;
            month = month - 12;
        }

        if (month < 10) {
            return ((Integer.toString(year).trim()) + '0' + ((Integer.toString(month).trim())));
        } else {
            return ((Integer.toString(year).trim()) + ((Integer.toString(month).trim())));
        }
    }

    /**
     * calculate the difference year between two dates
     *
     * @param startDate String
     * @param endDate   String
     * @return int
     */
    public static int calculateDifferenceYear(final Date startDate, final Date endDate) {
        assert startDate != null && endDate != null : "startDate or endDate is null";

        final String start = formatDate(startDate);
        final String end = formatDate(endDate);

        final int month = calculateDifferenceMonth(start, end);
        int year = month / 12;

        if ((month % 12) == 0) {
            final String startDay = start.substring(6);
            final String endDay = end.substring(6);
            if (startDay.compareTo(endDay) > 0) {
                year = year - 1;
            }
        }

        return year;
    }

    /**
     * Get the year of birthday from IDCard Number
     *
     * @param id IdCard Number
     * @return int - year
     */
    public static int getYearFromIdCard(final String id) {
        final int length = id.length();
        String year;

        if (length == 15) {
            year = id.substring(6, 8);
            year = "19" + year;
        } else if (length == 18) {
            year = id.substring(6, 10);
        } else {
            throw new IllegalArgumentException("error IDCard number");
        }
        return Integer.parseInt(year);
    }

    /**
     * Get birthday from IDCard Number
     *
     * @param id IDCard Number
     * @return birthday
     */
    public static String getBirthDayFromIdCard(final String id) {
        final int idLength = id.length();
        String year, month, day;

        final String today = (formatDate(new Date(), "yyyy-MM-dd"));

        if (idLength == 15) {
            year = "19" + id.substring(6, 8);
            month = id.substring(8, 10);
            day = id.substring(10, 12);
        } else if (idLength == 18) {
            year = id.substring(6, 10);
            month = id.substring(10, 12);
            day = id.substring(12, 14);
        } else {
            return today;
        }

        if (verityDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day))) {
            return year + "-" + month + "-" + day;
        }

        return today;
    }

    /**
     * Format date in Chinese
     *
     * @param d Date
     * @return String
     */
    public static String formatDateInChinese(final Date d) {
        if (d == null) {
            return "";
        }
        final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY, new DateFormatSymbols());

        final String dtrDate = df.format(d);
        return dtrDate.substring(0, 4) + "年" + dtrDate.substring(4, 6) + "月"
                + dtrDate.substring(6, 8) + "日";
    }

    /**
     * Get current date
     *
     * @return date, type of java.util.Date
     */
    public static Date getCurrentDate() {
        final Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * Get current timestamp，format yyyy-mm-dd HH:mi:ss
     *
     * @return timestamp string
     */
    public static String getCurrentDateTimeString() {
        SimpleDateFormat sf = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date date = new Date();
        return sf.format(date);
    }

    /**
     * Format timestamp to yyyy-MM-dd HH:mm:ss
     *
     * @return timestamp string
     */
    public static String formatTimestamp(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return sdf.format(new Date(l));
    }

    /**
     * Get current date in format yyyyMMdd
     *
     * @return String of current date
     */
    public static String getCurrentDateString() {
        final Calendar cal = Calendar.getInstance();

        String currentMonth, currentDay;

        final String currentYear = (Integer.valueOf(cal.get(Calendar.YEAR))).toString();

        if (cal.get(Calendar.MONTH) < 9) {
            currentMonth = "0" + (Integer.valueOf(cal.get(Calendar.MONTH) + 1)).toString();
        } else {
            currentMonth = (Integer.valueOf(cal.get(Calendar.MONTH) + 1)).toString();
        }

        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            currentDay = "0" + (Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH))).toString();
        } else {
            currentDay = (Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH))).toString();
        }

        return currentYear + currentMonth + currentDay;
    }

    /**
     * Get the current date in the specified format
     *
     * @param format String - date format
     * @return String of current date
     */
    public static String formatCurrentDate(final String format) {
        final Calendar cal = Calendar.getInstance();

        final Date d = cal.getTime();

        return formatDate(d, format);
    }

    /**
     * Get current year
     *
     * @return year
     */
    public static int getCurrentYear() {
        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * Get current year and month date string
     *
     * @return date, format yyyyMM
     */
    public static String getCurrentYearMonth() {
        final Calendar cal = Calendar.getInstance();
        final String currentYear = (Integer.valueOf(cal.get(Calendar.YEAR))).toString();
        String currentMonth;
        if (cal.get(Calendar.MONTH) < 9) {
            currentMonth = "0" + (cal.get(Calendar.MONTH) + 1);
        } else {
            currentMonth = Integer.valueOf(cal.get(Calendar.MONTH) + 1).toString();
        }
        return (currentYear + currentMonth);
    }

    /**
     * get current sql date
     *
     * @return sql date
     */
    public static java.sql.Date getCurrentSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * Get the first day of next month
     *
     * @return date string
     */
    public static String getFirstDayOfThisMonth() {
        final String strToday = getCurrentDateString();
        return nextMonthWithYear(strToday.substring(0, 6)) + "01";
    }

    // Get gender from id card
    public static String getGenderByIdCard(final String iDCard) {
        int gender = 3;
        if (iDCard.length() == 15) {
            gender = Integer.parseInt(iDCard.substring(14, 15)) % 2;
        } else if (iDCard.length() == 18) {
            gender = Integer.parseInt(iDCard.substring(16, 17)) % 2;
        }
        if (gender == 1) {
            return "1";
        } else if (gender == 0) {
            return "2";
        } else {
            return "";
        }
    }

    /**
     * Get difference day from start date to end date
     *
     * @param startDate calculate from this date
     * @param endDate   calculate to this date
     * @return difference day
     */
    public static int calculateDifferenceDay(final java.sql.Date startDate, final java.sql.Date endDate) {
        final long start = startDate.getTime();
        final long end = endDate.getTime();
        final long interval = end - start;
        return (int) ( interval / 24000 / 3600);
    }
    
    /**
     * Get last day of the month
     *
     * @param date date string in yyyyMMdd
     * @return last day of month
     */
    public static String getLastDayOfMonth(final String date) {

        final int year = Integer.parseInt(date.substring(0, 4));
        final int month = Integer.parseInt(date.substring(4, 6));

        String getLastDay;

        if (month == 2) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                getLastDay = "29";
            } else {
                getLastDay = "28";
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            getLastDay = "30";
        } else {
            getLastDay = "31";
        }
        return year + (month > 10 ? "" + month : "0" + month) + getLastDay;
    }

    /**
     * Get the date after adding the specified number of days
     *
     * @param date  date
     * @param after number of months
     * @return date
     */
    public static java.sql.Date getDateAfterDay(final java.sql.Date date, final int after) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, after);
        return new java.sql.Date(calendar.getTime().getTime());
    }

    /**
     * Get the date after adding the specified number of days
     *
     * @param date  date
     * @param after number of months
     * @return date
     */
    public static Date getDateAfterDay(final Date date, final int after) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, after);
        return new Date(calendar.getTime().getTime());
    }

    /**
     * Get the date after adding the specified number of months
     *
     * @param date  date
     * @param after number of months
     * @return date
     */
    public static java.sql.Date getDateAfterMonth(final java.sql.Date date, final int after) {
        final Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.MONTH, after);
        return new java.sql.Date(calo.getTime().getTime());
    }

    /**
     * Get the date after adding the specified number of months
     *
     * @param date  date
     * @param after number of months
     * @return date
     */
    public static Date getDateAfterMonth(final Date date, final int after) {
        final Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.MONTH, after);
        return new Date(calo.getTime().getTime());
    }

    /**
     * Get the date after adding the specified number of years
     *
     * @param date  date
     * @param after number of years
     * @return date
     */
    public static java.sql.Date getDateAfterYear(final java.sql.Date date, final int after) {
        final Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.YEAR, after);
        return new java.sql.Date(calo.getTime().getTime());
    }

    /**
     * Get the date after adding the specified number of years
     *
     * @param date  date
     * @param after number of years
     * @return date
     */
    public static Date getDateAfterYear(final Date date, final int after) {
        final Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(Calendar.YEAR, after);
        return new Date(calo.getTime().getTime());
    }

    /**
     * get date from java.util.Date
     *
     * @param date date
     * @return date of year
     */
    public static String formatDate(final Date date) {
        return new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY).format(date);
    }

    /**
     * Reformat date from without center line to with center line.
     * from yyyyMMDD to yyyy-MM-dd
     *
     * @param strDate date String, in format yyyyMMdd
     * @return date string, in format yyyy-MM-dd
     */
    public static String reformatWithCenterLine(final String strDate) {
        try {
            return strDate.substring(0, 4) + "-" + strDate.substring(4, 6) + "-" + strDate.substring(6, 8);
        } catch (final Exception e) {
            return strDate;
        }
    }

    /**
     * Get date string in format yyyy-MM
     *
     * @param date java.sql.Date
     * @return date string
     */
    public static String getSqlDateYearMonth(final java.sql.Date date) {
        if (date == null) {
            return null;
        }
        final String dateStr = date.toString();
        return dateStr.substring(0, 7);
    }

    /**
     * Get the number of year and month, format yyyyMM
     *
     * @param date date
     * @return number of year and month
     */
    public static int convertYearMonthToInt(final Date date) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc.get(GregorianCalendar.YEAR) * 100 + gc.get(GregorianCalendar.MONTH) + 1;
    }

    /**
     * Convert date from 'yyyyMM' format to 'yyyy年MM月' format
     *
     * @param date year and month
     * @return format date
     */
    public static String covertYearMonthInChinese(final String date) {
        if (null == date) {
            return null;
        }
        final String ym = date.trim();
        if (6 != ym.length()) {
            return ym;
        }
        final String year = ym.substring(0, 4);
        return year + "年" + date.substring(4) + "月";
    }

    /**
     * Get the previous month date string
     *
     * @param date sql data
     * @return date string format by 'yyyy-MM'
     */
    public static String getSqlPreviousYearMonth(final java.sql.Date date) {
        assert date != null;

        final String dateStr = date.toString();

        String str = dateStr.substring(0, 4) + dateStr.substring(5, 7);

        str = getPreviousMonthDate(str);

        return str.substring(0, 4) + "-" + str.substring(4, 6);
    }

    /**
     * Get next month date string format by yyyyMM
     *
     * @param yearMonth date string
     * @return date string
     */
    public static String nextMonthWithYear(final String yearMonth) {
        if (yearMonth == null || yearMonth.length() < 6) {
            return null;
        }
        char[] chars = yearMonth.toCharArray();

        if (chars[4] == '1' && chars[5] == '2') {
            chars[4] = '0';
            chars[5] = '1';
            chars[3] += 1;
        } else if (chars[5] == '9') {
            chars[4] = '1';
            chars[5] = '0';
        } else {
            chars[5] += 1;
        }

        return new String(chars);
    }

    /**
     * Convert the input integer type months into a string in the format of "X年X个月"
     *
     * @param month Integer
     * @return String
     */
    public static String formatMonthsInChinese(final Integer month) {

        assert month != null && month >= 0 : "month must be greater than or equal to 0";
        String yearMonth = "";

        int year, over;

        year = month / 12;
        over = month % 12;

        if (year > 0) {
            yearMonth = year + "年";
        }
        if (over > 0) {
            yearMonth += over + "个月";
        }

        return yearMonth;
    }

    /**
     * Converts a string in the specified format to a date type
     *
     * @param strDate    date string
     * @param dateFormat data format
     * @return date
     */
    public static Date parseStringToDate(final String strDate, final String dateFormat) {
        if (strDate == null) {
            return null;
        }

        final SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        Date myDate;
        try {
            myDate = df.parse(strDate);
        } catch (final Exception e) {
            return null;
        }

        return myDate;
    }

    /**
     * Converts a string in the specified format to a date type
     *
     * @param strDate date string
     * @return date
     */
    public static Date parseStringToDate(final String strDate) {
        if (strDate == null) {
            return null;
        }

        final SimpleDateFormat df = new SimpleDateFormat();

        Date myDate;
        try {
            myDate = df.parse(strDate);
        } catch (final Exception e) {
            return null;
        }

        return myDate;
    }

    /**
     * convert oracle data format to java data format
     *
     * @param oracleFormat oracle data format
     * @return java data format
     */
    public static String convertOracleFormatToJavaFormat(String oracleFormat) {
        StringBuilder javaFormat = new StringBuilder();

        final StringBuilder builder = new StringBuilder(oracleFormat.toLowerCase()).append(" ");
        int start = 0;
        for (int index = 0; index < builder.length(); index++) {
            if (ORACLE_FORMAT_SPLIT.contains(builder.charAt(index))) {
                String format = builder.substring(start, index);
                switch (format) {
                    case "mon":
                    case "mm":
                        format = "MM";
                        break;
                    case "hh24":
                        format = "HH";
                        break;
                    case "mi":
                        format = "mm";
                        break;
                    case "rrrr":
                        format = "yyyy";
                        break;
                    case "rr":
                        format = "yy";
                        break;
                    case "ff3":
                        format = "SSS";
                        break;
                }
                javaFormat.append(format).append(builder.charAt(index));
                start = index + 1;
            }
        }
        javaFormat.deleteCharAt(javaFormat.length() - 1);
        return javaFormat.toString();
    }

    /**
     * Convert the string into number according to the format of 'yyyyMMdd'
     *
     * @param date date string
     * @return number
     */
    public static Integer convertStringToNum(final String date) {

        assert date != null && !date.isEmpty();

        final String temp = date.replaceAll("-", "");
        return Integer.valueOf(temp);
    }

    /**
     * Checks whether the given year, month, and day is a valid date.
     *
     * @param yyyy year in yyyy format
     * @param MM   month of year, start from 1
     * @param dd   day of month
     * @return true if the date is valid, false otherwise
     */
    public static boolean verityDate(final int yyyy, final int MM, final int dd) {

        // Determine whether it is 4, 6, 9 or 11
        if (MM == 4 || MM == 6 || MM == 9 || MM == 11) {
            return dd > 0 && dd <= 30;

        } else if (MM == 2) { // February

            // is leap year
            if (yyyy % 100 != 0 && yyyy % 4 == 0 || yyyy % 400 == 0) {
                return dd > 0 && dd <= 29;
            }

            // not leap year
            return dd > 0 && dd <= 28;
        }

        return MM > 0 && MM <= 12 && dd > 0 && dd <= 31;
    }

    /**
     * Convert the string into sql time type according to the default date format
     *
     * @param dateString datetime string
     * @param dateFormat date format
     * @return sql data
     */
    public static java.sql.Date parseStringToSqlDate(final String dateString, final String dateFormat) {
        final Date date = parseStringToDate(dateString, dateFormat);

        if (date == null) {
            return null;
        }

        return new java.sql.Date(date.getTime());
    }

    /**
     * Convert the string into sql time type according to the default date format
     *
     * @param strDate date string
     * @return sql date
     */
    public static java.sql.Date parseStringToSqlDate(String strDate) {
        return parseStringToSqlDate(strDate, "yyyy-MM-dd");
    }

    /**
     * Convert the string into sql time type according to the default date format
     *
     * @param strDate datetime string
     * @return time
     */
    public static java.sql.Time parseStringToSqlTime(String strDate) {
        return parseStringToSqlTime(strDate, TIME_FORMAT);
    }

    /**
     * Convert the string into sql time type according to the specified date format
     *
     * @param strDate    datetime string
     * @param timeFormat date format
     * @return time
     */
    public static java.sql.Time parseStringToSqlTime(String strDate, String timeFormat) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }
        assert timeFormat != null;
        Date date = new SimpleDateFormat(timeFormat).parse(strDate, new ParsePosition(0));
        return convertDateToSqlTime(date);
    }

    /**
     * Convert the string into sql time stamp type according to the default date format
     *
     * @param strDate datetime string
     * @return timestamp
     */
    public static java.sql.Timestamp parseStringToSqlTimestamp(String strDate) {
        return parseStringToSqlTimestamp(strDate, TIMESTAMP_FORMAT);
    }

    /**
     * Convert the string into sql time stamp type according to the specified date format
     *
     * @param strDate    datetime string
     * @param dateformat datetime format
     * @return timestamp
     */
    public static java.sql.Timestamp parseStringToSqlTimestamp(String strDate, String dateformat) {
        assert dateformat != null;

        if (strDate == null) {
            return null;
        }

        Date date = new SimpleDateFormat(dateformat).parse(strDate, new ParsePosition(0));
        return convertDateToSqlTimestamp(date);
    }

    /**
     * Convert date type to sql date type
     *
     * @param date date to convert
     * @return converted date
     */
    public static java.sql.Date convertDateToSqlDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return new java.sql.Date(date.getTime());
        }
    }

    /**
     * Convert date type to sql time type
     *
     * @param date date to convert
     * @return converted time
     */
    public static java.sql.Time convertDateToSqlTime(Date date) {
        if (date == null) {
            return null;
        } else {
            return new java.sql.Time(date.getTime());
        }
    }

    /**
     * Convert date type to sql time stamp type
     *
     * @param date date to convert
     * @return converted timestamp
     */
    public static java.sql.Timestamp convertDateToSqlTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return new java.sql.Timestamp(date.getTime());
        }
    }

}
