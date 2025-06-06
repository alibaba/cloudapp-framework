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

package io.cloudapp.api.messaging;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThreadLocalTraceStorageTest {
    
    private ThreadLocalTraceStorage threadLocalTraceStorageUnderTest;
    
    @Before
    public void setUp() throws Exception {
        threadLocalTraceStorageUnderTest = new ThreadLocalTraceStorage();
    }
    
    @Test
    public void testSetTraceId() {
        // Setup
        // Run the test
        threadLocalTraceStorageUnderTest.setTraceId("traceId");
        
        // Verify the results
    }
    
    @Test
    public void testGetTraceId() {
        threadLocalTraceStorageUnderTest.setTraceId("spanId");
        assertEquals("spanId", threadLocalTraceStorageUnderTest.getTraceId());
    }
    
    @Test
    public void testSetSpanId() {
        // Setup
        // Run the test
        threadLocalTraceStorageUnderTest.setSpanId("spanId");
        
        // Verify the results
    }
    
    @Test
    public void testGetSpanId() {
        assertEquals("spanId", threadLocalTraceStorageUnderTest.getSpanId());
    }
    
    @Test
    public void testClearTrace() {
        // Setup
        // Run the test
        threadLocalTraceStorageUnderTest.clearTrace();
        
        // Verify the results
    }
    
    @Test
    public void testClearSpan() {
        // Setup
        // Run the test
        threadLocalTraceStorageUnderTest.clearSpan();
        
        // Verify the results
    }
    
}
