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
package com.alibaba.cloudapp.scheduler.test.schedulerx2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.ListGroupsRequest;
import com.aliyun.schedulerx220190430.models.ListGroupsResponse;
import com.aliyun.schedulerx220190430.models.ListJobsRequest;
import com.aliyun.schedulerx220190430.models.ListJobsResponse;
import com.alibaba.cloudapp.api.scheduler.model.ExecuteMode;
import com.alibaba.cloudapp.api.scheduler.model.Job;
import com.alibaba.cloudapp.api.scheduler.model.JobType;
import com.alibaba.cloudapp.api.scheduler.model.TimeType;
import com.alibaba.cloudapp.api.scheduler.server.JobManager;
import com.alibaba.cloudapp.util.RandomStringGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SchedulerX2JobManagerSit {
    
    private static final Logger logger = LoggerFactory.getLogger(
            SchedulerX2JobManagerSit.class);
    
    @Autowired
    JobManager jobManager;
    
    private String namespace = "983693cc-fb92-4b83-b197-1245440ff429";
    
    private String groupId = "test-app20241210";
    
    private String jobName = "test-job";
    
    private Long jobId = 29296269L;
    
    @Test
    public void getDelegatingClient() {
        Client delegatingClient = (Client) jobManager.getDelegatingClient();
    
        ListJobsRequest request = new ListJobsRequest();
        request.setNamespace(namespace);
        request.setGroupId(groupId);
        request.setRegionId("public");
        try {
            ListJobsResponse jobs = delegatingClient.listJobs(
                    request);
            if (jobs != null & !jobs.getBody().getData().getJobs().isEmpty()) {
                jobs.getBody().getData().getJobs().stream().forEach(
                        item -> logger.info("Job is: {}",
                                            JSONObject.toJSONString(item)
                        ));
            
            } else {
                logger.info("List Job result is empty, please create a " +
                                    "Job in the schedulerx console " +
                                    "first .");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void get() {
        Job job = jobManager.get(namespace, groupId, jobId);
        logger.info("get job detail: {}", JSON.toJSONString(job));
    }
    
    @Test
    public void list() {
/*
//        nested exception is io.cloudapp.exeption.CloudAppException: java.lang.NullPointerException
     List list = jobManager.list(namespace, groupId, "", "");
     List list = jobManager.list(namespace, groupId, jobName, "");
*/
        List list = jobManager.list(namespace, groupId, jobName, "0");
        list.stream().forEach(
                e -> logger.info("list job detail: {}", JSON.toJSONString(e)));
    }
    
    @Test
    public void create() {
        Job job = new Job();
        job.setNamespace(namespace);
        job.setGroupId(groupId);
        job.setName("test-job" + RandomStringGenerator.generate(3));
        job.setDescription("job desc..");
        job.setExecuteMode(ExecuteMode.STANDALONE.getKey());

//        job type, required
//        job.setJobType(JobType.JAVA);
//        job.setClassName("io.cloudapp.scheduler.test.schedulerx2.JobTest");

//        job.setJobType(JobType.SHELL);
//        job.setContent("echo 'shell test..'");

//        job.setJobType(JobType.PYTHON);
//        job.setContent("print('python test..')");
        
        job.setJobType(JobType.HTTP);
        String content = "{\"url\":\"https://www.baidu.com\"," +
                "\"method\":\"GET\",\"respParseMode\":1,\"respCode\":200,\"timeout\":5,\"httpJobExecutionMode\":0}";
        job.setContent(content);


//        time type, required
//        job.setTimeType(TimeType.CRON);
//        job.setTimeExpression("0 0/1 * * * ?");
        
        job.setTimeType(TimeType.NONE);
        job.setTimeExpression("");
//
//        job.setTimeType(TimeType.FIXED_RATE);
//        job.setTimeExpression("60000");
//
//        job.setTimeType(TimeType.FIXED_DELAY);
//        job.setTimeExpression("60000");

//        Map contacts = new HashMap();
////        contacts.put("UserPhone", "");
//        contacts.put("UserName", "test");
////        contacts.put("UserMail", "");
////        contacts.put("Ding", "");
//        job.setContacts(contacts);
        
        Long jobId = jobManager.create(job);
        logger.info("create job id: {}", JSON.toJSONString(jobId));
        logger.info("create job detail: {}",
                    JSON.toJSONString(jobManager.get(namespace, groupId, jobId))
        );
    }
    
    @Test
    public void update() {
        Long id = 29287424L;
        Job job = new Job();
        job.setJobId(id);
        job.setNamespace(namespace);
        job.setGroupId(groupId);
        job.setTimeType(TimeType.NONE);
        
        job.setDescription(
                "update desc..  " + RandomStringGenerator.generate(3));
        
        boolean update = jobManager.update(job);
        logger.info("update job result: {}", JSON.toJSONString(update));
        logger.info("update job detail: {}",
                    JSON.toJSONString(jobManager.get(namespace, groupId, id))
        );
    }
    
    @Test
    public void disable() {
        boolean disable = jobManager.disable(namespace, groupId, jobId);
        logger.info("disable job result: {}", JSON.toJSONString(disable));
    }
    
    @Test
    public void enable() {
        boolean enable = jobManager.enable(namespace, groupId, jobId);
        logger.info("enable job result: {}", JSON.toJSONString(enable));
    }
    
    @Test
    public void execute() {
        String params = "";
        Long execute = jobManager.execute(namespace, groupId, jobId, params);
        logger.info("execute job result: {}", JSON.toJSONString(execute));
    }
    
    @Test
    public void delete() {
        boolean delete = jobManager.delete(namespace, groupId, jobId);
        logger.info("delete job result: {}", delete);
    }
    
}
