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

package com.alibaba.cloudapp.scheduler.schedulerx2;


import com.aliyun.schedulerx220190430.Client;
import com.aliyun.schedulerx220190430.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.alibaba.cloudapp.api.scheduler.model.JobGroup;
import com.alibaba.cloudapp.exeption.CloudAppException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerX2JobGroupManagerTest {

    @Mock
    private Client client;

    @InjectMocks
    private SchedulerX2JobGroupManager jobGroupManager;

    @Before
    public void setUp() {
        jobGroupManager = new SchedulerX2JobGroupManager(client);
    }

    @Test
    public void create_ValidInputs_Success() throws Exception {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace("namespace");
        jobGroup.setAppName("appName");
        jobGroup.setGroupId("groupId");
        jobGroup.setDescription("description");

        CreateAppGroupResponse response = new CreateAppGroupResponse();
        CreateAppGroupResponseBody responseBody = new CreateAppGroupResponseBody();
        responseBody.setData(new CreateAppGroupResponseBody.CreateAppGroupResponseBodyData());
        responseBody.getData().setAppGroupId(123L);
        response.setBody(responseBody);

        when(client.createAppGroupWithOptions(any(CreateAppGroupRequest.class), any(RuntimeOptions.class)))
                .thenReturn(response);

        Long result = jobGroupManager.create(jobGroup);

        assertNotNull(result);
        assertEquals(Long.valueOf(123L), result);
    }

    @Test(expected = CloudAppException.class)
    public void create_MissingNamespace_ThrowsException() throws Exception {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setAppName("appName");
        jobGroup.setGroupId("groupId");

        jobGroupManager.create(jobGroup);
    }

    @Test(expected = CloudAppException.class)
    public void create_ClientThrowsException_ThrowsCloudAppException() throws Exception {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace("namespace");
        jobGroup.setAppName("appName");
        jobGroup.setGroupId("groupId");

        when(client.createAppGroupWithOptions(any(CreateAppGroupRequest.class), any(RuntimeOptions.class)))
                .thenThrow(new RuntimeException("Client error"));

        jobGroupManager.create(jobGroup);
    }

    @Test
    public void get_ValidInputs_Success() throws Exception {
        String namespace = "namespace";
        String groupId = "groupId";

        GetAppGroupResponse response = new GetAppGroupResponse();
        GetAppGroupResponseBody responseBody = new GetAppGroupResponseBody();
        responseBody.setData(new GetAppGroupResponseBody.GetAppGroupResponseBodyData());
        responseBody.getData().setGroupId(groupId);
        response.setBody(responseBody);

        when(client.getAppGroup(any(GetAppGroupRequest.class)))
                .thenReturn(response);

        JobGroup result = jobGroupManager.get(namespace, groupId);

        assertNotNull(result);
        assertEquals(groupId, result.getGroupId());
    }

    @Test(expected = CloudAppException.class)
    public void get_MissingNamespace_ThrowsException() throws Exception {
        jobGroupManager.get("", "groupId");
    }

    @Test(expected = CloudAppException.class)
    public void get_ClientThrowsException_ThrowsCloudAppException() throws Exception {
        String namespace = "namespace";
        String groupId = "groupId";

        when(client.getAppGroup(any(GetAppGroupRequest.class)))
                .thenThrow(new RuntimeException("Client error"));

        jobGroupManager.get(namespace, groupId);
    }

    @Test
    public void delete_ValidInputs_Success() throws Exception {
        String namespace = "namespace";
        String groupId = "groupId";

        DeleteAppGroupResponse response = new DeleteAppGroupResponse();
        DeleteAppGroupResponseBody responseBody = new DeleteAppGroupResponseBody();
        responseBody.setSuccess(true);
        response.setBody(responseBody);

        when(client.deleteAppGroup(any(DeleteAppGroupRequest.class)))
                .thenReturn(response);

        boolean result = jobGroupManager.delete(namespace, groupId);

        assertTrue(result);
    }

    @Test(expected = CloudAppException.class)
    public void delete_MissingGroupId_ThrowsException() throws Exception {
        jobGroupManager.delete("namespace", "");
    }

    @Test(expected = CloudAppException.class)
    public void delete_ClientThrowsException_ThrowsCloudAppException() throws Exception {
        String namespace = "namespace";
        String groupId = "groupId";

        when(client.deleteAppGroup(any(DeleteAppGroupRequest.class)))
                .thenThrow(new RuntimeException("Client error"));

        jobGroupManager.delete(namespace, groupId);
    }

    @Test
    public void update_ValidInputs_Success() throws Exception {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace("namespace");
        jobGroup.setAppName("appName");
        jobGroup.setGroupId("groupId");
        jobGroup.setDescription("description");

        UpdateAppGroupResponse response = new UpdateAppGroupResponse();
        UpdateAppGroupResponseBody responseBody = new UpdateAppGroupResponseBody();
        responseBody.setSuccess(true);
        response.setBody(responseBody);

        when(client.updateAppGroup(any(UpdateAppGroupRequest.class)))
                .thenReturn(response);

        boolean result = jobGroupManager.update(jobGroup);

        assertTrue(result);
    }

    @Test(expected = CloudAppException.class)
    public void update_NullJobGroup_ThrowsException() throws Exception {
        jobGroupManager.update(null);
    }

    @Test(expected = CloudAppException.class)
    public void update_ClientThrowsException_ThrowsCloudAppException() throws Exception {
        JobGroup jobGroup = new JobGroup();
        jobGroup.setNamespace("namespace");
        jobGroup.setAppName("appName");
        jobGroup.setGroupId("groupId");

        when(client.updateAppGroup(any(UpdateAppGroupRequest.class)))
                .thenThrow(new RuntimeException("Client error"));

        jobGroupManager.update(jobGroup);
    }

    @Test
    public void list_ValidInputs_Success() throws Exception {
        String namespace = "namespace";
        String name = "name";

        ListGroupsResponse response = new ListGroupsResponse();
        ListGroupsResponseBody responseBody = new ListGroupsResponseBody();
        ListGroupsResponseBody.ListGroupsResponseBodyData data = new ListGroupsResponseBody.ListGroupsResponseBodyData();
        ListGroupsResponseBody.ListGroupsResponseBodyDataAppGroups appGroup = new ListGroupsResponseBody.ListGroupsResponseBodyDataAppGroups();
        appGroup.setGroupId("groupId");
        data.setAppGroups(new ArrayList<>());
        data.getAppGroups().add(appGroup);
        responseBody.setData(data);
        response.setBody(responseBody);

        when(client.listGroups(any(ListGroupsRequest.class)))
                .thenReturn(response);

        List<JobGroup> result = jobGroupManager.list(namespace, name);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("groupId", result.get(0).getGroupId());
    }

    @Test(expected = CloudAppException.class)
    public void list_MissingNamespace_ThrowsException() throws Exception {
        jobGroupManager.list("", "name");
    }

    @Test(expected = CloudAppException.class)
    public void list_ClientThrowsException_ThrowsCloudAppException() throws Exception {
        String namespace = "namespace";
        String name = "name";

        when(client.listGroups(any(ListGroupsRequest.class)))
                .thenThrow(new RuntimeException("Client error"));

        jobGroupManager.list(namespace, name);
    }
}
