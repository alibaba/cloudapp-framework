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
package com.alibaba.cloudapp.microservice.aliyun;

import com.alibaba.fastjson2.JSON;
import com.alibaba.arms.tracing.Span;
import com.alibaba.arms.tracing.Tracer;

import java.util.*;

public class ArmsTagTool {

    public static final String LANE_TAG = "__microservice_tag__";
    // CANARY_TAG is immutable, only can be read
    public static final String CANARY_TAG = "__microservice_match_result__";

    public static String getTraceId() {
        return Tracer.builder().getSpan().getTraceId();
    }

    public static Map<String, String> baggageItems() {
        return Tracer.builder().getSpan().baggageItems();
    }

    static class Tag {
        private String name;

        private String tag;

        private int priority;

        public Tag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    public static String withCanaryTag(String tag) {
        List<ArmsTagTool.Tag> tagList = constructTag(tag);
        String mergeTagStr = JSON.toJSONString(tagList);
        putBaggageItem(LANE_TAG, mergeTagStr);
        return tag;
    }
    
    public static void clearCanaryTag() {
        putBaggageItem(LANE_TAG, "");
    }

    public static void putBaggageItem(String key, String value) {
        Map<String, String> maps = new HashMap<>();
        maps.put(key, value);
        putBaggageItems(maps);
    }

    public static void putBaggageItems(Map<String, String> pairs) {
        Span span = Tracer.builder().getSpan();
        // get current span tags
        Map<String, String> tags = span.baggageItems();
        // merge baggage items
        tags.putAll(pairs);
        // set new baggage into span
        span.withBaggage(tags);
    }

    public static String originalTrafficTag(String labelKey) {
        return Tracer.builder().getSpan().baggageItems().get(labelKey);
    }

    private static List<ArmsTagTool.Tag> constructTag(String tag) {
        List<ArmsTagTool.Tag> tagList = new ArrayList<>();
        ArmsTagTool.Tag t = new ArmsTagTool.Tag(tag);
        t.setPriority(100);
        tagList.add(t);
        return tagList;
    }
}
