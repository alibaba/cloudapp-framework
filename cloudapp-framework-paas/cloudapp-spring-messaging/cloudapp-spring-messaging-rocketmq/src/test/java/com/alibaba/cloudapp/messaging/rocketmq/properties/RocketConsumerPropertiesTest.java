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

import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RocketConsumerPropertiesTest {
    
    private RocketConsumerProperties properties;
    
    @Before
    public void setUp() {
        properties = new RocketConsumerProperties();
        properties.setName("name");
        properties.setTopic("topic");
        properties.setMessageModel(MessageModel.CLUSTERING);
        properties.setTraceTopic("traceTopic");
        properties.setTags(Collections.singletonList("tag"));
        properties.setUsername("username");
        properties.setPassword("password");
        properties.setNameServer("nameServer");
        properties.setDefault(true);
        properties.setEnableMsgTrace(true);
    }
    
    @Test
    public void testTypeGetterAndSetter() {
        final String type = "type";
        properties.setType(type);
        assertEquals(type, properties.getType());
    }
    
    @Test
    public void testAccessChannelGetterAndSetter() {
        final String accessChannel = "accessChannel";
        properties.setAccessChannel(accessChannel);
        assertEquals(accessChannel,
                     properties.getAccessChannel()
        );
    }
    
    @Test
    public void testGroupGetterAndSetter() {
        final String group = "group";
        properties.setGroup(group);
        assertEquals(group, properties.getGroup());
    }
    
    @Test
    public void testMessageModelGetterAndSetter() {
        properties.setMessageModel(MessageModel.BROADCASTING);
        assertEquals(MessageModel.BROADCASTING,
                     properties.getMessageModel()
        );
    }
    
    @Test
    public void testPullBatchSizeGetterAndSetter() {
        final int pullBatchSize = 0;
        properties.setPullBatchSize(pullBatchSize);
        assertEquals(pullBatchSize,
                     properties.getPullBatchSize()
        );
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
    public void testNameGetterAndSetter() {
        final String name = "name";
        properties.setName(name);
        assertEquals(name, properties.getName());
    }
    
    @Test
    public void testTopicGetterAndSetter() {
        final String topic = "topic";
        properties.setTopic(topic);
        assertEquals(topic, properties.getTopic());
    }
    
    @Test
    public void testTagsGetterAndSetter() {
        final List<String> tags = Collections.singletonList("value");
        properties.setTags(tags);
        assertEquals(tags, properties.getTags());
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
    public void testIsDefaultGetterAndSetter() {
        final boolean isDefault = false;
        properties.setDefault(isDefault);
        assertFalse(properties.isDefault());
    }
    
}
