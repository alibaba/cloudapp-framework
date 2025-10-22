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

package com.alibaba.cloudapp.api.microservice;

import io.opentelemetry.context.Scope;

import java.util.Map;

/**
 * Two major sensarios related to this document: <a href="https://help.aliyun.com/zh/edas/user-guide/use-the-edas-console-to-implement-canary-releases-of-applications-in-kubernetes-clusters-2">
 *     EDAS Canary Updates</a>, and <a href="https://help.aliyun.com/zh/edas/user-guide/full-link-canary-grayscale-release-using-full-link-swimlanes-k8s">
 *     EDAS Full Link Grey Scaling Support</a>
 */
public interface TrafficService {

    /**
     * Check if traffic is supported, for general senarios, it is supported by
     * a traffic system like Alibaba Arms product, skywalking, open telemetry, etc.
     *
     * @return true if traffic is supported, false otherwise
     */
    boolean supportTrafficManagement();

    /**
     * Check if the current request is marked with a traffic label, a traffic
     * label usually is a string value, set by the upstreaming application, based
     * on the traffic rules.
     *
     * @param labelValue the traffic label
     * @return true if the request is marked successfully, false otherwise
     */
    boolean currentTrafficMatchWith(String labelValue);

    /**
     * Get the current traffic label value. This may not be null when traffic is
     * during EDAS canary release or in the EDAS swimming lane or .
     * @return the value of current traffic label
     */
    String getCurrentTrafficLabel();

    /**
     * Get the current environment label, which is a string value, set manually via
     * the startup parameters, or set automatically by the application platform
     * like Alibaba EDAS.
     *
     * @return the current running process environment label.
     */
    String getCurrentEnvironmentLabel();

    /**
     * Check if the current process is during a canary release.
     *
     * @return true if the current process is during a canary release, false otherwise.
     */
    boolean isDuringCanaryRelease();

    /**
     * Check if the current process is warming up during a microservice updates.
     * for EDAS product, please refer to the link: <a href="https://help.aliyun.com/zh/edas/user-guide/warm-up">
     *   EDAS Warm Up Support</a>
     *
     * @return true if the current process is under heat, false otherwise.
     */
    boolean isInstanceWarmingUp();

    /**
     * Build opentelemetry scope object to specified where traffic label will be brought.
     * @param labelValue the value of traffic label
     * @return return the opentelemetry scope object, used by try...final .
     */
    void withTrafficLabel(String labelValue);

    /**
     * Get the current user requesting values, e.g: the user-data in an eagleeye
     * context request
     *
     * @return a map of key-value pairs
     */
    Map<String, String> getBaggageUserData();

    /**
     * Get the current user requesting value, e.g: the user-data value in an
     * eagleeye context
     *
     * @param key The key of the parameter
     * @return The value of the parameter
     */
    String getBaggageUserDataValue(String key);

    /**
     * Build opentelemetry scope object to specified where user-data will be brought.
     * @param pairs key-value pairs of user-data that brought by baggage
     * @return return the opentelemetry scope object, used by try...final .
     */
    void withBaggageUserData(Map<String, String> pairs);

    /**
     * Get the current trace id, e.g: the trace-id in an eagleeye context request
     *
     * @return A string unique id.
     */
    String currentTraceId();
}
