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

package com.alibaba.cloudapp.datasource.druid;

import com.alibaba.cloudapp.api.observabilities.InvokeCount;
import com.alibaba.cloudapp.api.observabilities.Metric;
import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.api.observabilities.MetricType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@MetricCollection(serviceName = "demo-metric")
public class MetricsDemoController {
    
    public long getMyCounter() {
        return myCounter;
    }
    
    public void setMyCounter(long add) {
        this.myCounter = add;
    }
    
    @Metric(name="abc.counter", attributes = "interface=/add", type = MetricType.COUNTER)
    public long myCounter=0;
    
    AtomicLong counter = new AtomicLong(10);
    AtomicLong counter2 = new AtomicLong(15);
    
    @Autowired
    DemoMetricsHolder metricsHolder;
    
    @Autowired
    MetricsDemoController metricsDemoController;
    
    @RequestMapping("/add")
    public long counter() {
        metricsDemoController.setMyCounter(counter.getAndIncrement());
//        this.setMyCounter(syncCounter.getAndIncrement());
        System.out.println("this.myCounter is : " + this.myCounter);
        return this.myCounter;
    }
    
    @RequestMapping("/add2")
    public long counter2() {
        metricsHolder.setCounter(counter2.getAndIncrement());
        System.out.println("metricsHolder.getCounter is : " + metricsHolder.getCounter());
        return metricsHolder.getCounter();
    }
    
    @InvokeCount(name="abc.invoke",
            attributes = "one=args[0];two=args[1]")
    @RequestMapping("/invokeAdd/{one}/{two}")
    public String invoke(@PathVariable String one,
                       @PathVariable String two) {
        return "one is : " + one + ", two is : " + two;
    }
}
