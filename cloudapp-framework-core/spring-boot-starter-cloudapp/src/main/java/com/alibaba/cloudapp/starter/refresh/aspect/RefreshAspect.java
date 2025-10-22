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

package com.alibaba.cloudapp.starter.refresh.aspect;

import com.alibaba.cloudapp.starter.base.RefreshManager;
import com.alibaba.cloudapp.starter.refresh.PropKeyRefreshedEvent;
import com.alibaba.cloudapp.starter.refresh.RefreshableBeanDef;
import com.alibaba.cloudapp.starter.refresh.TargetRefreshable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Aspect
@EnableAspectJAutoProxy
public class RefreshAspect implements ApplicationListener<PropKeyRefreshedEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(RefreshAspect.class);

    private final Map<String, RefreshableBeanDef> joints = new ConcurrentHashMap<>();


    @Around("@annotation(RefreshableBinding)")
    public Object refreshPointcut(ProceedingJoinPoint joinPoint)
            throws Throwable {
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RefreshableBinding binding = method.getAnnotation(RefreshableBinding.class);
        
        String declareName = joinPoint.getSignature().getName();
        logger.info("Processing Refreshable Beans: {}", declareName);
        
        String key = binding.value();

        Object result = joinPoint.proceed();
        
        if (! StringUtils.hasText(key)) {
            logger.warn("Ignore processing refreshable beans as binding key is not found.");
            return result;
        }


        if (! (result instanceof TargetRefreshable)) {
            logger.warn("Ignore processing refreshable beans as the target bean is not a proxy.");
            return result;
        }

        RefreshableBeanDef def = new RefreshableBeanDef((TargetRefreshable) result);

        joints.put(key, def);
        
        return  result;
    }
    
    
    @Override
    public void onApplicationEvent(PropKeyRefreshedEvent event) {
        String key = event.getRefreshedKey();
        RefreshableBeanDef def = joints.get(key);
        logger.warn("Receive bean refreshing requests, key = {}.", key);

        if (def == null) {
            logger.warn("Ignore processing refreshable bean key {} is not found", key);
            RefreshManager.onPropertiesChanged(key);
            return;
        }
        
        def.refreshBean(event.getProperties());
    }

}
