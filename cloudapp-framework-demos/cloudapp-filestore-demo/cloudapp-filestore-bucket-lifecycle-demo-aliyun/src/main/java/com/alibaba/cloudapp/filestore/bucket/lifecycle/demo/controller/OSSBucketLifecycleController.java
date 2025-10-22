package com.alibaba.cloudapp.filestore.bucket.lifecycle.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.model.StorageClass;
import com.alibaba.cloudapp.api.filestore.BucketLifeCycleManager;
import com.alibaba.cloudapp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class OSSBucketLifecycleController {

    private final static Logger logger =
            LoggerFactory.getLogger(OSSBucketLifecycleController.class);

    @Autowired
    private BucketLifeCycleManager bucketLifeCycleManager;

    private String defaultBucketName = "test0610";

    @RequestMapping("/getBucketLifeCycle")
    public String getBucketLifeCycle(@RequestParam String bucketName) {
        Object bucketLifeCycle = bucketLifeCycleManager.getBucketLifeCycle(
                bucketName);
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        logger.info("bucket life cycle: {}", JSON.toJSONString(bucketLifeCycle));
        return JSON.toJSONString(bucketLifeCycle);
    }

    @RequestMapping("/expireObjectsAfterVersionNoncurrentDays")
    public void expireObjectsAfterVersionNoncurrentDays(@RequestParam String bucketName, @RequestParam String objectPrefixName, @RequestParam(defaultValue = "1") int noncurrentDays) {
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        bucketLifeCycleManager.expireObjectsAfterVersionNoncurrentDays(
                bucketName, objectPrefixName, noncurrentDays);
    }

    @RequestMapping("/expireObjectsWithCreateBefore")
    public void expireObjectsWithCreateBefore(@RequestParam String bucketName, @RequestParam String objectPrefixName) {
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        Date date = DateUtil.parseStringToDate("2024-09-01 08:12:00",
                "yyyy-MM-dd HH:mm:ss"
        );
        bucketLifeCycleManager.expireObjectsWithCreateBefore(bucketName, objectPrefixName, date);
        logger.info("create lifecycle rule expireObjectsWithCreateBefore success");
    }

    @RequestMapping("/expireObjectsWithLastAccessDays")
    public void expireObjectsWithLastAccessDays(@RequestParam String bucketName, @RequestParam String objectPrefixName, @RequestParam(defaultValue = "1") int lastAccessDays) {
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        bucketLifeCycleManager.expireObjectsWithLastAccessDays(defaultBucketName,
                objectPrefixName, lastAccessDays
        );
    }

//    TODO ERROR: Transition is not supported.
    @RequestMapping("/transitToWithLastAccessDays")
    public void transitToWithLastAccessDays(@RequestParam String bucketName, @RequestParam String objectPath, @RequestParam String storageClass, @RequestParam(defaultValue = "1") int lastAccessDays) {
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        bucketLifeCycleManager.transitToWithLastAccessDays(bucketName,
                objectPath,
                StorageClass.IA.toString(),
                lastAccessDays
        );
    }

    @RequestMapping("/deleteBucketLifeCycle")
    public boolean deleteBucketLifeCycle(@RequestParam String bucketName) {
        if (bucketName == null) {
            bucketName = defaultBucketName;
        }
        boolean result = bucketLifeCycleManager.deleteBucketLifeCycle(
                bucketName);
        logger.info("delete bucket lifecycle result is: {}", result);
        return result;
    }

}