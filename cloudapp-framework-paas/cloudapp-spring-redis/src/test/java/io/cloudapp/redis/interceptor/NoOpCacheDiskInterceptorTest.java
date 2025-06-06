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

package io.cloudapp.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NoOpCacheDiskInterceptorTest {

    @InjectMocks
    private NoOpCacheDiskInterceptor noOpCacheDiskInterceptor;

    private Logger logger;

    @Before
    public void setUp() {
        logger = mock(Logger.class);
        noOpCacheDiskInterceptor = new NoOpCacheDiskInterceptor();
    }

    @Test
    public void expire_KeyAndMillis_LogsDebugMessage() {
        byte[] key = "key".getBytes();
        long millis = 1000L;

        noOpCacheDiskInterceptor.expire(key, millis);

        verify(logger, times(0)).debug("expire key");
    }

    @Test
    public void persist_Key_LogsDebugMessage() {
        byte[] key = "key".getBytes();

        noOpCacheDiskInterceptor.persist(key);

        verify(logger, times(0)).debug("persist key");
    }

    @Test
    public void delete_Keys_LogsDebugMessage() {
        byte[] key1 = "key1".getBytes();
        byte[] key2 = "key2".getBytes();

        noOpCacheDiskInterceptor.delete(key1, key2);

        verify(logger, times(0)).debug("delete keys");
    }

    @Test
    public void notifyChanged_Keys_LogsDebugMessage() {
        byte[] key1 = "key1".getBytes();
        byte[] key2 = "key2".getBytes();
        Collection<byte[]> keys = Arrays.asList(key1, key2);

        noOpCacheDiskInterceptor.notifyChanged(keys);

        verify(logger, times(0)).debug("notifyChanged keys");
    }
}
