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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class FileUtils {
    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static Map readPropertiesFromFile(String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            logger.warn("Failed to read properties from file {}, file not exits",
                    filePath);
            return Collections.emptyMap();
        }

        if (!file.canRead()) {
            logger.warn("Failed to read properties from file {}, file can't read",
                    filePath);
            return Collections.emptyMap();
        }

        Properties properties = new Properties();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            properties.load(reader);
        } catch (Throwable t) {
            logger.warn("Failed to read properties from file {}", filePath, t);
        }

        if (properties.isEmpty()) {
            return Collections.emptyMap();
        }

        return properties;
    }
}
