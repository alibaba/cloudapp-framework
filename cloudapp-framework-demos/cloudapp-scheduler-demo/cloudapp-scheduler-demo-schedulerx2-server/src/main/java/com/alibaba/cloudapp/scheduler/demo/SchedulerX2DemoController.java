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

import com.alibaba.fastjson2.JSON;
import com.aliyun.schedulerx220190430.Client;
import com.alibaba.cloudapp.api.scheduler.model.*;
import com.alibaba.cloudapp.api.scheduler.server.JobExecuteService;
import com.alibaba.cloudapp.api.scheduler.server.JobGroupManager;
import com.alibaba.cloudapp.api.scheduler.server.JobManager;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchedulerX2DemoController {
    
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2DemoController.class);
    // Get from SchedulerX2 console
//    private final String namespaceId = "838d9c47-843e-40b8-bbdc-53808274b857";
    private final String namespaceId = "838d9c47-843e-40b8-bbdc-53808274b789";
    private final String groupId = "test-group2";
    private final String appName = "test-app2";
    private final String jobName = "test-job2";
    @Autowired
    @Qualifier("schedulerX2JobGroupManger")
    private JobGroupManager<Client> jobGroupManager;
    @Autowired
    private JobManager<Client> jobManager;
    @Autowired
    private JobExecuteService<Client> jobExecuteService;
    
    // =============================== JobGroup ===============================

//    TODO Error: The namespace parameter is not supported in apasara.
    @RequestMapping("/createJobGroup")
    public Long createJobGroup() throws CloudAppException {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace(namespaceId);
        jobGroup.setAppName(appName);
        jobGroup.setGroupId(groupId);
        jobGroup.setDescription("test group");
        
        Long result = jobGroupManager.create(jobGroup);
        logger.info("create jobGroup result: {}", JSON.toJSONString(result));
        
        return result;
    }
    
    @RequestMapping("/updateJobGroup")
    public void updateJobGroup() throws CloudAppException {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace(namespaceId);
        jobGroup.setAppName(appName);
        jobGroup.setGroupId(groupId);
        jobGroup.setDescription("test group, updated.");
        
        boolean result = jobGroupManager.update(jobGroup);
        logger.info("update jobGroup result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/getJobGroup")
    public void getJobGroup() throws CloudAppException {
        JobGroup jobGroup = jobGroupManager.get(namespaceId, groupId);
        logger.info("get jobGroup result: {}", JSON.toJSONString(jobGroup));
    }
    
    @RequestMapping("/listJobGroup")
    public void listJobGroup() throws CloudAppException {
        List<JobGroup> jobGroups = jobGroupManager.list(namespaceId, null);
        logger.info("list jobGroup result: {}", JSON.toJSONString(jobGroups));
    }
    
    // =============================== Job ===================================
    
    @RequestMapping("/createJob")
    public Long createJob() throws CloudAppException {
        Job job = new Job();
        job.setGroupId(groupId);
        job.setNamespace(namespaceId);
        job.setName(jobName);
        job.setClassName("io.cloudapp.scheduler.demo.service.DemoJob");
        job.setDescription("test job");
        job.setJobType(JobType.JAVA);
        job.setContent("{\"param1\":\"value1\"}");
        job.setExecuteMode(ExecuteMode.STANDALONE.getKey());
        job.setTimeType(TimeType.NONE);
        
        Long result = jobManager.create(job);
        logger.info("create job result: {}", JSON.toJSONString(result));
        
        return result;
    }
    
    @RequestMapping("/updateJob")
    public void updateJob(@RequestParam Long jobId) throws CloudAppException {
        Job job = new Job();
        job.setGroupId(groupId);
        job.setJobId(jobId);
        job.setNamespace(namespaceId);
        job.setName(jobName);
        job.setClassName("io.cloudapp.scheduler.demo.service.DemoJob");
        job.setDescription("test job, updated.");
        job.setJobType(JobType.JAVA);
        job.setTimeType(TimeType.NONE);
        
        boolean result = jobManager.update(job);
        logger.info("update job result: {}", JSON.toJSONString(result));
    }
    
    
    @RequestMapping("/listJob")
    public void listJob() throws CloudAppException {
        List<Job> jobs = jobManager.list(namespaceId, groupId, jobName, "1");
        logger.info("list job result: {}", JSON.toJSONString(jobs));
    }
    
    @RequestMapping("/getJob")
    public void getJob(@RequestParam Long jobId) throws CloudAppException {
        Job job = jobManager.get(namespaceId, groupId, jobId);
        logger.info("get job result: {}", JSON.toJSONString(job));
    }
    
    @RequestMapping("/disableJob")
    public void disableJob(@RequestParam Long jobId) throws CloudAppException {
        boolean result = jobManager.disable(namespaceId, groupId, jobId);
        logger.info("disable job result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/enableJob")
    public void enableJob(@RequestParam Long jobId) throws CloudAppException {
        boolean result = jobManager.enable(namespaceId, groupId, jobId);
        logger.info("enable job result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/executeJob")
    public Long executeJob(@RequestParam Long jobId) throws CloudAppException {
        Long instanceId = jobManager.execute(namespaceId, groupId, jobId, "");
        logger.info("execute job result: {}", JSON.toJSONString(instanceId));
        
        return instanceId;
    }
    
    // ========================== Job Execute ==========================
    
    @RequestMapping("/getJobInstance")
    public void getJobInstance(@RequestParam String groupId,
                               @RequestParam Long jobId,
                               @RequestParam Long instanceId)
            throws CloudAppException {
        JobInstance jobInstance = jobExecuteService.get(namespaceId,
                                                        groupId,
                                                        jobId,
                                                        instanceId
        );
        logger.info("get jobInstance result: {}",
                    JSON.toJSONString(jobInstance)
        );
    }
    
    @RequestMapping("/listJobInstance")
    public void listJobInstance(@RequestParam Long jobId)
            throws CloudAppException {
        List<JobInstance> jobInstances = jobExecuteService.list(namespaceId,
                                                                groupId, jobId
        );
        logger.info("list jobInstance result: {}",
                    JSON.toJSONString(jobInstances)
        );
    }
    
    @RequestMapping("/retryJobInstance")
    public void retryJobInstance(@RequestParam Long jobId,
                                 @RequestParam Long instanceId)
            throws CloudAppException {
        boolean result = jobExecuteService.retry(namespaceId, groupId, jobId,
                                                 instanceId
        );
        logger.info("retry jobInstance result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/stopJobInstance")
    public void stopJobInstance(@RequestParam Long jobId,
                                @RequestParam Long instanceId)
            throws CloudAppException {
        boolean result = jobExecuteService.stop(namespaceId, groupId, jobId,
                                                instanceId
        );
        logger.info("stop jobInstance result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/setSuccessJobInstance")
    public void setSuccessJobInstance(@RequestParam Long jobId,
                                      @RequestParam Long instanceId)
            throws CloudAppException {
        boolean result = jobExecuteService.setSuccess(namespaceId, groupId,
                                                      jobId, instanceId
        );
        logger.info("setSuccess jobInstance result: {}",
                    JSON.toJSONString(result)
        );
    }
    
    // =================== Clear jobGroup, job =========================
    @RequestMapping("/deleteJobGroup")
    public void deleteJobGroup() throws CloudAppException {
        boolean result = jobGroupManager.delete(namespaceId, groupId);
        logger.info("delete jobGroup result: {}", JSON.toJSONString(result));
    }
    
    @RequestMapping("/deleteJob")
    public void deleteJob(@RequestParam Long jobId) throws CloudAppException {
        boolean result = jobManager.delete(namespaceId, groupId, jobId);
        logger.info("delete job result: {}", JSON.toJSONString(result));
    }
    
}
