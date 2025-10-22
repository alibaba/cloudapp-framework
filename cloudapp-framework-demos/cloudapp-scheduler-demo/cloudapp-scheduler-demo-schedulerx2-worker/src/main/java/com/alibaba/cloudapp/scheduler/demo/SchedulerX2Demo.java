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

package com.alibaba.cloudapp.scheduler.demo;

import com.alibaba.cloudapp.api.scheduler.worker.GlobalJob;
import com.alibaba.cloudapp.api.scheduler.worker.GlobalJobHelper;
import com.alibaba.cloudapp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulerX2Demo {

    private static final boolean autoCreation = false;
    
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2Demo.class);
    
    @GlobalJob(cron = "0 0 1 */1 * ?",
            description = "backup data",
            autoCreateJob = autoCreation,
            autoDeleteJob = autoCreation)
    public void backup() {
        logger.info("Data backup logic..");
    }
    
    
    /**
     * Note:
     * preProcess and postProcess in schedulerx2 correspond to
     * pre and post, which will be executed multiple times.
     */
    @GlobalJob(cron = "0 * * * * ?",
            preProcess = "preMethodCloudApp",
            postProcess = "postMethodCloudApp",
            description = "test description",
            autoCreateJob = autoCreation,
            autoDeleteJob = autoCreation)
    public void withPreAndPostMethodCloudApp() {
        String jobParam = GlobalJobHelper.getJobParam();
        long jobId = GlobalJobHelper.getJobId();
        
        logger.info(
                "Execute withPreAndPostMethodCloudApp method success. now :{} . " +
                        "jobId:{}, jobParam:{}",
                DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_FORMAT),
                jobId, jobParam
        );
    }
    
    public void preMethodCloudApp() {
        String jobParam = GlobalJobHelper.getJobParam();
        long jobId = GlobalJobHelper.getJobId();
        
        logger.info("Execute preMethodCloudApp method success. now :{} . " +
                            "jobId:{}, jobParam:{}",
                    DateUtil.formatDate(new Date(),
                                        DateUtil.TIMESTAMP_FORMAT
                    ),
                    jobId, jobParam
        );
    }
    
    public void postMethodCloudApp() {
        String jobParam = GlobalJobHelper.getJobParam();
        long jobId = GlobalJobHelper.getJobId();
        
        logger.info("Execute postMethodCloudApp method success. now :{} . " +
                            "jobId:{}, jobParam:{}",
                    DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_FORMAT),
                    jobId, jobParam
        );
    }
    
    
    @GlobalJob(cron = "0 * * * * ?",
            executeMode = "sharding",
            shardingParams = "0=a,1=b",
            autoCreateJob = autoCreation,
            autoDeleteJob = autoCreation)
    public void shardMethodCloudApp() {
        String jobParam = GlobalJobHelper.getJobParam();
        long jobId = GlobalJobHelper.getJobId();
        int shardIndex = GlobalJobHelper.getShardIndex();
        int shardTotal = GlobalJobHelper.getShardTotal();
        String shardParameter = GlobalJobHelper.getShardParameter();
        
        logger.info("Execute shardMethodCloudApp method success. now :{} . " +
                            "jobId:{}, jobParam:{}, shardIndex:{}, " +
                            "shardTotal:{}, shardParameter:{}",
                    DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_FORMAT),
                    jobId, jobParam, shardIndex, shardTotal, shardParameter
        );
    }
    
    @GlobalJob(fixedDelay = 10,
            autoCreateJob = autoCreation,
            autoDeleteJob = autoCreation)
    public void fixedDelayMethodCloudApp() {
        logger.info("Execute fixedDelayMethodCloudApp method success. now :{}",
                    DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_FORMAT)
        );
    }
    
    @GlobalJob(value = "specifyFixedRateMethodCloudApp",
            fixedRate = 60,
            autoCreateJob = autoCreation,
            autoDeleteJob = autoCreation)
    public void fixedRateMethodCloudApp() {
        logger.info(
                "Execute specifyFixedRateMethodCloudApp method success. now :{}",
                DateUtil.formatDate(new Date(), DateUtil.TIMESTAMP_FORMAT)
        );
    }
    
}
