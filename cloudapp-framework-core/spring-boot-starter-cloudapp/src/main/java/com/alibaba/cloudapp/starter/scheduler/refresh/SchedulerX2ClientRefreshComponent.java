package com.alibaba.cloudapp.starter.scheduler.refresh;

import com.alibaba.schedulerx.common.util.ConfigUtil;
import com.alibaba.schedulerx.worker.SchedulerxWorker;
import com.alibaba.schedulerx.worker.domain.WorkerConstants;
import com.aliyun.schedulerx220190430.Client;
import com.aliyun.teaopenapi.models.Config;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.scheduler.schedulerx2.GlobalJobBeanRegistrar;
import com.alibaba.cloudapp.scheduler.schedulerx2.GlobalJobSyncManager;
import com.alibaba.cloudapp.starter.base.RefreshableComponent;
import com.alibaba.cloudapp.starter.refresh.RefreshableProxyFactory;
import com.alibaba.cloudapp.starter.scheduler.properties.SchedulerX2WorkerProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

public class SchedulerX2ClientRefreshComponent extends RefreshableComponent<
        SchedulerX2WorkerProperties, Client> {
    
    private SchedulerxWorker worker;
    private GlobalJobBeanRegistrar registrar;
    private GlobalJobSyncManager jobSyncManager;
    
    public SchedulerX2ClientRefreshComponent(SchedulerX2WorkerProperties properties) {
        super(properties);
    }
    
    @Override
    public void postStart() {
        try {
            RefreshableProxyFactory.updateProxyTarget(bean, properties);
        } catch (Exception e) {
            throw new CloudAppException("refresh SchedulerX2 error!", e);
        }
    }
    
    @Override
    public void preStop() {
    
    }
    
    @Override
    public String bindKey() {
        return SchedulerX2WorkerProperties.PREFIX;
    }
    
    @Override
    public String getName() {
        return "cloudAppSchedulerWorkClient";
    }
    
    @Override
    protected Client createBean(SchedulerX2WorkerProperties prop) {
        bean = RefreshableProxyFactory.create(this::creatClient, prop);
        registrar = new GlobalJobBeanRegistrar();
        worker = RefreshableProxyFactory.create(this::createWorker, prop);
        jobSyncManager = RefreshableProxyFactory.create(this::createJobSyncManager, prop);
        
        return bean;
    }
    
    @NotNull
    private Client creatClient(SchedulerX2WorkerProperties prop) {
        try {
            Assert.hasText(prop.getOpenAPIEndpoint(),
                           "SchedulerX2 openapi endpoint must be provided"
            );
            Assert.hasText(prop.getRegionId(),
                           "SchedulerX2 regionId must be provided"
            );
            Assert.hasText(prop.getAccessKeyId(),
                           "SchedulerX2 AccessKeyId must be provided"
            );
            Assert.hasText(prop.getAccessKeySecret(),
                           "SchedulerX2 AccessKeySecret must be provided"
            );
            
            Config config = new Config()
                    .setEndpoint(prop.getOpenAPIEndpoint())
                    .setRegionId(prop.getRegionId())
                    .setAccessKeyId(prop.getAccessKeyId())
                    .setAccessKeySecret(prop.getAccessKeySecret());
            
            return new Client(config);
        } catch (Exception ex) {
            throw new CloudAppException("Failed to create SchedulerX2 client"
                    , ex);
        }
    }
    
    private SchedulerxWorker createWorker(SchedulerX2WorkerProperties prop) {
        SchedulerxWorker schedulerxWorker = new SchedulerxWorker();
        
        if(prop.getEndpoint() != null) {
            schedulerxWorker.setEndpoint(prop.getEndpoint());
        }
        if(prop.getDomainName() != null){
            schedulerxWorker.setDomainName(prop.getDomainName());
        }
        if(prop.getNamespace() != null) {
            schedulerxWorker.setNamespace(prop.getNamespace());
        }
        schedulerxWorker.setGroupId(prop.getGroupId());
        schedulerxWorker.setAppKey(prop.getAppKey());
        
        // Set the boot mode
        ConfigUtil.getWorkerConfig().setProperty(WorkerConstants.WORKER_STARTER_MODE,
                                                 WorkerConstants.WORKER_STARTER_SPRINGBOOT);
        return schedulerxWorker;
    }
    
    private GlobalJobSyncManager createJobSyncManager(SchedulerX2WorkerProperties prop) {
        return new GlobalJobSyncManager(bean,
                                        prop.getRegionId(),
                                        prop.getNamespace(),
                                        prop.getGroupId()
        );
    }
    
    public SchedulerxWorker getWorker() {
        return worker;
    }
    
    public GlobalJobSyncManager getJobSyncManager() {
        return jobSyncManager;
    }
    
    public GlobalJobBeanRegistrar getRegistrar() {
        return registrar;
    }
}
