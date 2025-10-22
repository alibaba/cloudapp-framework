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

package com.alibaba.cloudapp.sequence;

public class Constants {

    public static final String DEFAULT_QUEUE_NAME = "io.cloudapp.default.queue";

    public static final String SPRING_REDIS_ROOT = "spring.redis";

    public static final String SEQUENCE_CONFIG_ROOT = "io.cloudapp.sequence";

    public static final String SEQUENCE_CONFIG_REDIS_ROOT = SEQUENCE_CONFIG_ROOT + ".redis";

    public static final String SEQUENCE_CONFIG_SNOWFLAKE_ROOT = SEQUENCE_CONFIG_ROOT + ".snowflake";

    public static final String SEQUENCE_CONFIG_JEDIS_ROOT = SEQUENCE_CONFIG_REDIS_ROOT + ".jedis";

    public static final String SEQUENCE_CONFIG_LETTUCE_ROOT = SEQUENCE_CONFIG_REDIS_ROOT + ".lettuce";

}
