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

import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DateUtilTest {
    
    @Test
    public void testReformatDateWithoutCenterLine() {
        assertEquals("20200101",
                     DateUtil.reformatDateWithoutCenterLine("2020-01-01")
        );
        assertEquals("",
                     DateUtil.reformatDateWithoutCenterLine(null)
        );
    }
    
    @Test
    public void testCalculateDifferenceMonth() {
        assertEquals(37,
                     DateUtil.calculateDifferenceMonth("202302", "202001")
        );
    }
    
    @Test
    public void testGetYearOfDate() {
        assertEquals(2020, DateUtil.getYearOfDate(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testFormatDateToYYYYMM() {
        assertEquals("202001", DateUtil.formatDateToYYYYMM(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testFormatDateToYYYYMMDD() {
        assertEquals("20200101", DateUtil.formatDateToYYYYMMDD(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testFormatDate1() {
        assertEquals("2020/01/01", DateUtil.formatDate(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                "yyyy/MM/dd"
        ));
    }
    
    @Test
    public void testCalculateDifferenceDay1() {
        assertEquals(0, DateUtil.calculateDifferenceDay(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()
        ));
        
        assertEquals(0, DateUtil.calculateDifferenceDay(
                new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()
        ));
    }
    
    @Test
    public void testGetPreviousMonthDate() {
        assertEquals("201912", DateUtil.getPreviousMonthDate("202001"));
        
        assertEquals("202011", DateUtil.getPreviousMonthDate("202012"));
        assertEquals("202009", DateUtil.getPreviousMonthDate("202010"));
    }
    
    @Test
    public void testGetDateAfterMonth1() {
        assertEquals("202001", DateUtil.getDateAfterMonth("202001", 0));
        assertEquals("202201", DateUtil.getDateAfterMonth("202009", 16));
        assertEquals("202112", DateUtil.getDateAfterMonth("202009", 15));
    }
    
    @Test
    public void testCalculateDifferenceYear() {
        assertEquals(0, DateUtil.calculateDifferenceYear(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()
        ));
    }
    
    @Test
    public void testGetYearFromIdCard() {
        assertEquals(1992, DateUtil.getYearFromIdCard("101410199201028914"));
        assertEquals(1992, DateUtil.getYearFromIdCard("101410920102891"));
        assertThrows(IllegalArgumentException.class, () ->
                DateUtil.getYearFromIdCard("1014101992010289"));
    }
    
    @Test
    public void testGetBirthDayFromIdCard() {
        assertEquals("1992-01-02", DateUtil.getBirthDayFromIdCard(
                "101410199201028914"));
        assertEquals("1992-01-02",
                     DateUtil.getBirthDayFromIdCard("101410920102891")
        );
        assertEquals(LocalDate.now().toString(),
                     DateUtil.getBirthDayFromIdCard("")
        );
    }
    
    @Test
    public void testFormatDateInChinese() {
        assertEquals("2020年01月01日", DateUtil.formatDateInChinese(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testGetCurrentDate() {
        assertEquals(DateUtil.getCurrentDate().getTime() / 1000,
                     System.currentTimeMillis() / 1000
        );
    }
    
    @Test
    public void testGetCurrentDateTimeString() {
        assertEquals(LocalDateTime.now()
                                  .format(DateTimeFormatter.ofPattern(
                                          DateUtil.TIMESTAMP_FORMAT)),
                     DateUtil.getCurrentDateTimeString()
        );
    }
    
    @Test
    public void testFormatTimestamp() {
        assertEquals("1970-01-01 08:00:00", DateUtil.formatTimestamp(0L));
    }
    
    @Test
    public void testGetCurrentDateString() {
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern(
                             "yyyyMMdd")),
                     DateUtil.getCurrentDateString()
        );
    }
    
    @Test
    public void testFormatCurrentDate() {
        assertEquals(LocalDate.now().toString(),
                     DateUtil.formatCurrentDate(DateUtil.DATE_FORMAT)
        );
    }
    
    @Test
    public void testGetCurrentYear() {
        assertEquals(DateUtil.getCurrentYear(), DateUtil.getCurrentYear());
    }
    
    @Test
    public void testGetCurrentYearMonth() {
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern(
                "yyyyMM")), DateUtil.getCurrentYearMonth());
    }
    
    @Test
    public void testGetCurrentSqlDate() {
        assertEquals(DateUtil.getCurrentSqlDate().getTime(), System.currentTimeMillis());
    }
    
    @Test
    public void testGetFirstDayOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = calendar.get(Calendar.MONTH) + 1 > 9 ?
                String.valueOf(calendar.get(Calendar.MONTH) + 1)
                : "0" + (calendar.get(Calendar.MONTH) + 1);
        
        assertEquals(year + month + "01", DateUtil.getFirstDayOfThisMonth());
    }
    
    @Test
    public void testGetGenderByIdCard() {
        assertEquals("1", DateUtil.getGenderByIdCard("100812199410222852"));
        assertEquals("2", DateUtil.getGenderByIdCard("100812941022276"));
        assertEquals("", DateUtil.getGenderByIdCard("10081294102227"));
    }
    
    @Test
    public void testCalculateDifferenceDay2() {
        assertEquals(366, DateUtil.calculateDifferenceDay(
                Date.valueOf(LocalDate.of(2020, 1, 1)),
                Date.valueOf(LocalDate.of(2021, 1, 1))
        ));
        assertEquals(-366, DateUtil.calculateDifferenceDay(
                Date.valueOf(LocalDate.of(2021, 1, 1)),
                Date.valueOf(LocalDate.of(2020, 1, 1))
        ));
    }
    
    @Test
    public void testGetLastDayOfMonth() {
        assertEquals("20200229", DateUtil.getLastDayOfMonth("20200203"));
        assertEquals("20210331", DateUtil.getLastDayOfMonth("20210303"));
        assertEquals("20201130", DateUtil.getLastDayOfMonth("20201103"));
        assertEquals("21000228", DateUtil.getLastDayOfMonth("21000220"));
    }
    
    @Test
    public void testGetDateAfterDay1() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.getDateAfterDay(
                             Date.valueOf(LocalDate.of(2020, 1, 1)), 0)
        );
    }
    
    @Test
    public void testGetDateAfterDay2() {
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     DateUtil.getDateAfterDay(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime(), 0)
        );
    }
    
    @Test
    public void testGetDateAfterMonth2() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.getDateAfterMonth(
                             Date.valueOf(LocalDate.of(2020, 1, 1)), 0)
        );
    }
    
    @Test
    public void testGetDateAfterMonth3() {
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     DateUtil.getDateAfterMonth(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime(), 0)
        );
    }
    
    @Test
    public void testGetDateAfterYear1() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.getDateAfterYear(
                             Date.valueOf(LocalDate.of(2020, 1, 1)), 0)
        );
    }
    
    @Test
    public void testGetDateAfterYear2() {
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     DateUtil.getDateAfterYear(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime(), 0)
        );
    }
    
    @Test
    public void testFormatDate2() {
        assertEquals("20200101", DateUtil.formatDate(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testReformatWithCenterLine() {
        assertEquals("2020-01-01",
                     DateUtil.reformatWithCenterLine("20200101")
        );
        assertEquals("day",
                     DateUtil.reformatWithCenterLine("day")
        );
    }
    
    @Test
    public void testGetSqlDateYearMonth() {
        assertEquals("2020-01", DateUtil.getSqlDateYearMonth(
                Date.valueOf(LocalDate.of(2020, 1, 1))));
        assertNull(DateUtil.getSqlDateYearMonth(null));
    }
    
    @Test
    public void testConvertYearMonthToInt() {
        assertEquals(202001, DateUtil.convertYearMonthToInt(
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime()));
    }
    
    @Test
    public void testCovertYearMonthInChinese() {
        assertEquals("2020年01月", DateUtil.covertYearMonthInChinese("202001"));
        assertEquals("2020", DateUtil.covertYearMonthInChinese("2020"));
        assertNull(DateUtil.covertYearMonthInChinese(null));
    }
    
    @Test
    public void testGetSqlPreviousYearMonth() {
        assertEquals("2019-12", DateUtil.getSqlPreviousYearMonth(
                Date.valueOf(LocalDate.of(2020, 1, 1))));
    }
    
    @Test
    public void testNextMonthWithYear() {
        assertEquals("202004", DateUtil.nextMonthWithYear("202003"));
        assertEquals("202101", DateUtil.nextMonthWithYear("202012"));
    }
    
    @Test
    public void testFormatMonthsInChinese() {
        assertEquals("1个月", DateUtil.formatMonthsInChinese(1));
        assertEquals("1年3个月", DateUtil.formatMonthsInChinese(15));
    }
    
    @Test
    public void testParseStringToDate1() {
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     DateUtil.parseStringToDate("2020-01-01",
                                                DateUtil.DATE_FORMAT
                     )
        );
        assertNull(DateUtil.parseStringToDate(null));
        assertNull(DateUtil.parseStringToDate("date"));
    }
    
    @Test
    public void testParseStringToDate2() {
        String date = new SimpleDateFormat().format(Date.valueOf("2020-01-01"));
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     DateUtil.parseStringToDate(date)
        );
    }
    
    @Test
    public void testConvertOracleFormatToJavaFormat() {
        assertEquals("yyyy-MM-dd HH:mm:ss.SSS",
                     DateUtil.convertOracleFormatToJavaFormat(
                             "rrrr-mm-dd hh24:mi:ss.ff3")
        );
        assertEquals("yy-MM-dd",
                     DateUtil.convertOracleFormatToJavaFormat(
                             "rr-mm-dd")
        );
    }
    
    @Test
    public void testConvertStringToNum() {
        assertEquals(Integer.valueOf(2020),
                     DateUtil.convertStringToNum("2020")
        );
    }
    
    @Test
    public void testVerityDate() {
        assertFalse(DateUtil.verityDate(0, 0, 0));
        assertFalse(DateUtil.verityDate(2021, 2, 29));
        assertFalse(DateUtil.verityDate(2020, 4, 31));
        assertFalse(DateUtil.verityDate(2100, 2, 29));
    }
    
    @Test
    public void testParseStringToSqlDate1() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.parseStringToSqlDate("2020-01-01",
                                                   DateUtil.DATE_FORMAT
                     )
        );
        assertNull(DateUtil.parseStringToSqlDate(null));
        
    }
    
    @Test
    public void testParseStringToSqlDate2() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.parseStringToSqlDate("2020-01-01")
        );
    }
    
    @Test
    public void testParseStringToSqlTime1() {
        assertEquals(Time.valueOf(LocalTime.of(0, 0, 0)),
                     DateUtil.parseStringToSqlTime("00:00:00")
        );
        assertNull(DateUtil.parseStringToSqlTime(null));
    }
    
    @Test
    public void testParseStringToSqlTime2() {
        assertEquals(Time.valueOf(LocalTime.of(0, 0, 0)).toString(),
                     DateUtil.parseStringToSqlTime("2020-01-01",
                                                   DateUtil.DATE_FORMAT
                     ).toString()
        );
    }
    
    @Test
    public void testParseStringToSqlTimestamp1() {
        assertEquals(
                Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)),
                DateUtil.parseStringToSqlTimestamp("2020-01-01 00:00:00")
        );
        assertNull(DateUtil.parseStringToSqlTimestamp(null));
    }
    
    @Test
    public void testParseStringToSqlTimestamp2() {
        assertEquals(
                Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)),
                DateUtil.parseStringToSqlTimestamp("2020-01-01T00:00:00",
                                                   "yyyy-MM-dd'T'HH:mm:ss"
                )
        );
    }
    
    @Test
    public void testConvertDateToSqlDate() {
        assertEquals(Date.valueOf(LocalDate.of(2020, 1, 1)),
                     DateUtil.convertDateToSqlDate(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime())
        );
        assertNull(DateUtil.convertDateToSqlDate(null));
    }
    
    @Test
    public void testConvertDateToSqlTime() {
        assertEquals(Time.valueOf(LocalTime.of(0, 0, 0)).toString(),
                     DateUtil.convertDateToSqlTime(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime()).toString()
        );
        assertNull(DateUtil.convertDateToSqlTime(null));
    }
    
    @Test
    public void testConvertDateToSqlTimestamp() {
        assertEquals(
                Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)),
                DateUtil.convertDateToSqlTimestamp(
                        new GregorianCalendar(2020, Calendar.JANUARY,
                                              1
                        ).getTime())
        );
        assertNull(DateUtil.convertDateToSqlTimestamp(null));
    }
    
}
