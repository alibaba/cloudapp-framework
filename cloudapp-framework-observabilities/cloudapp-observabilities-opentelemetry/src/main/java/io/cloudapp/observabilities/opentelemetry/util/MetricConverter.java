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

package io.cloudapp.observabilities.opentelemetry.util;

import io.cloudapp.api.observabilities.InvokeCount;
import io.cloudapp.api.observabilities.Metric;
import io.cloudapp.api.observabilities.MetricType;

import java.lang.annotation.Annotation;

public class MetricConverter {
    public static Metric convert(InvokeCount count) {
        if (count == null) {
            return null;
        }
        String name = count.name();
        String description = count.description();
        String unit = count.unit();
        String attributes = count.attributes();

        return new Metric() {
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return Metric.class;
            }
            
            @Override
            public String name() {
                return name;
            }

            @Override
            public String attributes() {
                return attributes;
            }
            
            @Override
            public MetricType type() {
                return MetricType.COUNTER;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String unit() {
                return unit;
            }
            
            /**
             * InvokeCount is not async, so return false.
             */
            @Override
            public boolean async() {
                return false;
            }
            
            @Override
            public double[] buckets() {
                return null;
            }
        };
        
    }
}
