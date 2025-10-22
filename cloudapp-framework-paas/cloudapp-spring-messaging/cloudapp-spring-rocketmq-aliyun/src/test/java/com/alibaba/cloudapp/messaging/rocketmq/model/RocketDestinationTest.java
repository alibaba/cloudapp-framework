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

package com.alibaba.cloudapp.messaging.rocketmq.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RocketDestinationTest {
    
    private RocketDestination rocketDestinationUnderTest;
    
    @Before
    public void setUp() {
        rocketDestinationUnderTest = new RocketDestination(
                "topic", Collections.singletonList("value")
        );
    }
    
    @Test
    public void testGetTopic() {
        assertEquals("topic", rocketDestinationUnderTest.getTopic());
    }
    
    @Test
    public void testAddTag() {
        // Setup
        // Run the test
        rocketDestinationUnderTest.addTag("tag");
        
        // Verify the results
    }
    
    @Test
    public void testAddTags() {
        // Setup
        // Run the test
        rocketDestinationUnderTest.addTags(Collections.singletonList("value"));
        
        // Verify the results
    }
    
    @Test
    public void testGetTags() {
        assertEquals(new HashSet<>(Collections.singletonList("value")),
                     rocketDestinationUnderTest.getTags()
        );
    }
    
    @Test
    public void testGetTagsString() {
        // Setup
        // Run the test
        final String result = rocketDestinationUnderTest.getTagsString();
        
        // Verify the results
        assertEquals("value", result);
    }
    
    @Test
    public void testEquals() {
        assertNotEquals("o", rocketDestinationUnderTest);
    }
    
    
}
