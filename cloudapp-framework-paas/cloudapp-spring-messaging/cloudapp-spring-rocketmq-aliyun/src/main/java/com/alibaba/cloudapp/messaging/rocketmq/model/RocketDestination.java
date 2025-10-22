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

import com.alibaba.cloudapp.api.messaging.model.Destination;

import java.util.*;

public class RocketDestination implements Destination {

    private final String topic;

    private final HashSet<String> tags = new HashSet<>();

    public RocketDestination(String topic) {
        this.topic = topic;
    }

    public RocketDestination(String topic, List<String> tags) {
        this.topic = topic;
        addTags(tags);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void addTag(String tag) {
        if(!"*".equals(tag)) {
            this.tags.add(tag);
        }
    }
    
    public void resetAllTags() {
        this.tags.clear();
    }

    public void addTags(List<String> tags) {
        if(tags != null) {
            tags.forEach(this::addTag);
        }
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public String getTagsString() {
        return tags.isEmpty() ? "*" : String.join("||", tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        RocketDestination that = (RocketDestination) o;
        return Objects.equals(topic, that.topic) &&
                tags.size() == that.getTags().size() &&
                new HashSet<>(tags).containsAll(that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, tags);
    }
    
    public boolean isContains(RocketDestination ds) {
        if(!topic.equals(ds.getTopic())) {
            return false;
        }
        return tags.isEmpty() || ds.getTags().isEmpty()
                || tags.containsAll(ds.getTags())
                || ds.getTags().containsAll(tags);
    }
}
