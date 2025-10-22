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

package com.alibaba.cloudapp.messaging.rocketmq.properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RocketProducerPropertiesTest {
    
    private RocketProducerProperties properties;
    
    @Before
    public void setUp() {
        properties = new RocketProducerProperties();
        properties.setDefault(true);
        properties.setName("name");
        properties.setGroup("group");
        properties.setSendTimeout(0);
        properties.setRetryTimesWhenSendFailed(0);
        properties.setEnableMsgTrace(true);
        properties.setTraceTopic("traceTopic");
        properties.setUseTLS(true);
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setMaxMessageSize(0);
        properties.setCompressMsgBodyOverHowMuch(0);
        properties.setNameServer("nameServer");
        
    }
    
    @Test
    public void testGroupGetterAndSetter() {
        final String group = "group";
        properties.setGroup(group);
        assertEquals(group, properties.getGroup());
    }
    
    @Test
    public void testNamespaceGetterAndSetter() {
        final String namespace = "namespace";
        properties.setNamespace(namespace);
        assertEquals(namespace,
                     properties.getNamespace()
        );
    }
    
    @Test
    public void testSendTimeoutGetterAndSetter() {
        final int sendTimeout = 0;
        properties.setSendTimeout(sendTimeout);
        assertEquals(sendTimeout,
                     properties.getSendTimeout()
        );
    }
    
    @Test
    public void testRetryNextServerGetterAndSetter() {
        final boolean retryNextServer = false;
        properties.setRetryNextServer(retryNextServer);
        assertFalse(properties.getRetryNextServer());
    }
    
    @Test
    public void testCompressMsgBodyOverHowMuchGetterAndSetter() {
        final int compressMsgBodyOverHowMuch = 0;
        properties.setCompressMsgBodyOverHowMuch(
                compressMsgBodyOverHowMuch);
        assertEquals(compressMsgBodyOverHowMuch,
                     properties.getCompressMsgBodyOverHowMuch()
        );
    }
    
    @Test
    public void testMaxMessageSizeGetterAndSetter() {
        final int maxMessageSize = 0;
        properties.setMaxMessageSize(maxMessageSize);
        assertEquals(maxMessageSize,
                     properties.getMaxMessageSize()
        );
    }
    
    @Test
    public void testRetryTimesWhenSendFailedGetterAndSetter() {
        final int retryTimesWhenSendFailed = 0;
        properties.setRetryTimesWhenSendFailed(
                retryTimesWhenSendFailed);
        assertEquals(retryTimesWhenSendFailed,
                     properties.getRetryTimesWhenSendAsyncFailed()
        );
    }
    
    @Test
    public void testRetryTimesWhenSendFailed1GetterAndSetter() {
        final int retryTimesWhenSendFailed = 0;
        properties.setRetryTimesWhenSendFailed(
                retryTimesWhenSendFailed);
        assertEquals(retryTimesWhenSendFailed,
                     properties.getRetryTimesWhenSendFailed()
        );
    }
    
    @Test
    public void testRetryNextServer1GetterAndSetter() {
        final boolean retryNextServer = false;
        properties.setRetryNextServer(retryNextServer);
        assertFalse(properties.isRetryNextServer());
    }
    
    @Test
    public void testNameServerGetterAndSetter() {
        final String nameServer = "nameServer";
        properties.setNameServer(nameServer);
        assertEquals(nameServer,
                     properties.getNameServer()
        );
    }
    
    @Test
    public void testUsernameGetterAndSetter() {
        final String username = "username";
        properties.setUsername(username);
        assertEquals(username, properties.getUsername());
    }
    
    @Test
    public void testPasswordGetterAndSetter() {
        final String password = "password";
        properties.setPassword(password);
        assertEquals(password, properties.getPassword());
    }
    
    @Test
    public void testUseTLSGetterAndSetter() {
        final boolean useTLS = false;
        properties.setUseTLS(useTLS);
        assertFalse(properties.isUseTLS());
    }
    
    @Test
    public void testEnableMsgTraceGetterAndSetter() {
        final Boolean enableMsgTrace = false;
        properties.setEnableMsgTrace(enableMsgTrace);
        assertFalse(properties.isEnableMsgTrace());
    }
    
    @Test
    public void testTraceTopicGetterAndSetter() {
        final String traceTopic = "traceTopic";
        properties.setTraceTopic(traceTopic);
        assertEquals(traceTopic,
                     properties.getTraceTopic()
        );
    }
    
    @Test
    public void testNameGetterAndSetter() {
        final String name = "name";
        properties.setName(name);
        assertEquals(name, properties.getName());
    }
    
    @Test
    public void testIsDefaultGetterAndSetter() {
        final boolean isDefault = false;
        properties.setDefault(isDefault);
        assertFalse(properties.isDefault());
    }
    
}
