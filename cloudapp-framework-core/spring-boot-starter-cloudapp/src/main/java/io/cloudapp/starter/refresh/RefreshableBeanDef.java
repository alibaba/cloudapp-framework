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

package io.cloudapp.starter.refresh;

import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshableBeanDef {
    
    private static final Logger logger = LoggerFactory.getLogger(RefreshableBeanDef.class);
    
    private TargetRefreshable refresher;
    
    public RefreshableBeanDef(TargetRefreshable point) {
        this.refresher = point;
    }
    
    @SuppressWarnings("unchecked")
    public void refreshBean(RefreshableProperties properties) {
        try {
            this.refresher.refreshTarget(properties);
        } catch (Throwable e) {
            logger.error("Could NOT BE HERE !!!It's a BUG!!RefreshableBeanDef.refreshBean() failed",
                         e);
        }
    }
}
