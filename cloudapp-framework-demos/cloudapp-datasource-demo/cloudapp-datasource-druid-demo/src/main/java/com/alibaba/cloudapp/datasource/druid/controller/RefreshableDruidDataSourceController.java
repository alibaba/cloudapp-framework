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
package com.alibaba.cloudapp.datasource.druid.controller;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.cloudapp.datasource.druid.RefreshableDruidDataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class RefreshableDruidDataSourceController {
    
    private static final Logger logger =
            LoggerFactory.getLogger(RefreshableDruidDataSourceController.class);
    
    @Resource
    private RefreshableDruidDataSourceWrapper refreshableDataSource;
    
    @RequestMapping("/freshDruid")
    public void freshDruid() {
        long maxWait = refreshableDataSource.getMaxWait();
        logger.info("maxWait is : {} .", maxWait);
        
        long maxRefreshWaitMillis = refreshableDataSource.getMaxRefreshWaitMillis();
        logger.info("maxRefreshWaitMillis is : {} .", maxRefreshWaitMillis);
        
    }
    
    @RequestMapping("/user/{username}")
    public boolean getUser(@PathVariable String username) {
        DruidPooledConnection connection = null;
        try {
            connection = refreshableDataSource.getConnection();
            String sql = "SELECT * FROM user WHERE username = '" + username + "'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("user {} exist.", username);
                return true;
            } else {
                logger.info("user {} not exist.", username);
                return false;
            }
        } catch (SQLException e) {
            logger.error("error", e);
        }
        
        return false;
    }
    
}
