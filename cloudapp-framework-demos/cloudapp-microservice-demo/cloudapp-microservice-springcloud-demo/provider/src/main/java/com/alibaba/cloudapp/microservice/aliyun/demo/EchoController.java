/**
 * Copyright (c) 2022-present Alibaba Group
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.alibaba.cloudapp.microservice.aliyun.demo;

import com.alibaba.cloudapp.api.microservice.TrafficService;
import com.alibaba.cloudapp.util.NetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class EchoController {
    @Autowired
    private TrafficService trafficService;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/ping")
    public boolean ping(){
        try {
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return false;
    }

    @RequestMapping(value = "/provider-echo/{str}")
    public String pingPongPing(@PathVariable String str) {
        long start = System.currentTimeMillis();

        String result = str;

        String[] arrays = result.split("\r\n");

        StringBuffer sb = new StringBuffer(start + " Provider echo method received\r\n");
        for (String val : arrays) {
            sb.append("\t" + val).append("\r\n");
        }

        long end = System.currentTimeMillis();

        sb.append(end + " Provider echo method return.");

        return sb.toString();
    }



    @RequestMapping(value = "/echo/{str}")
    public String echo(@PathVariable String str){
        long start = System.currentTimeMillis();

        try {
            TimeUnit.MILLISECONDS.sleep(appConfig.getTimeoutInMillis());
        } catch (InterruptedException e) {
        }

        boolean isCanary = trafficService.isDuringCanaryRelease();

        long end = System.currentTimeMillis();
        return "\r\n\t" + start + " Provider received (canary?  " + isCanary+ ", label: "+appConfig.getLabel()+")." +
            "\r\n\t\tProvider processed after sleep 1 second! Echo String: \"" + str + "\"" +
            "\r\n\t" + end + " Provider Return(host: " + NetUtil.getLocalIp() + ")";
    }


    @RequestMapping(value = "/tag2")
    public String tag2(){
        long start = System.currentTimeMillis();

        String currentTrafficLabel = trafficService.getCurrentTrafficLabel();

        System.out.println("currentTrafficLabel tag is :" + currentTrafficLabel);
        try {
            TimeUnit.MILLISECONDS.sleep(appConfig.getTimeoutInMillis());
        } catch (InterruptedException e) {
        }



//        boolean isCanary = trafficService.isDuringCanaryRelease();

        long end = System.currentTimeMillis();
        return "\r\n\t" + start + " Provider received (currentTrafficLabel?  " + currentTrafficLabel+ ")";
    }
}
