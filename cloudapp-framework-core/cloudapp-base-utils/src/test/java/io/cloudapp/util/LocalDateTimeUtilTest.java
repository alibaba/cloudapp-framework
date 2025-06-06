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

package io.cloudapp.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class LocalDateTimeUtilTest {
    
    @Test
    public void testGetLocalDateTime() {
        assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                     LocalDateTimeUtil.getLocalDateTime(
                             new GregorianCalendar(2020, Calendar.JANUARY,
                                                   1
                             ).getTime())
        );
    }
    
    @Test
    public void testGetDate() {
        assertEquals(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                     LocalDateTimeUtil.getDate(
                             LocalDateTime.of(2020, 1, 1, 0, 0, 0))
        );
    }
    
}
