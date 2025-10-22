/**
 * Copyright (c) 2022-present Alibaba Group
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.alibaba.cloudapp.microservice.aliyun.demo;

import com.alibaba.cloudapp.api.microservice.TrafficService;
import io.opentelemetry.context.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EchoService echoService;

    @Autowired
    private TrafficService trafficService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Boolean ping() {
        try {
            String pong = echoService.echo("ping", "");

            System.out.println("Service returned: " + pong);
            return pong.contains("ping");
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }


    @RequestMapping(value = "/tag2", method = RequestMethod.GET)
    public Boolean tag() {
        try {
            trafficService.withTrafficLabel("ok");
            System.out.println("consumer tag is : " + trafficService.getCurrentTrafficLabel());

            String pong = echoService.tag2();
            System.out.println("Service returned: " + pong);
            return pong.contains("ok");
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }


    @RequestMapping(value = "/consumer-echo/{str}", method = RequestMethod.GET)
    public String feign1(@PathVariable String str, @RequestParam(required = false) String env) {
        long start = System.currentTimeMillis();

        String result = echoService.echo(str, env);

        long end = System.currentTimeMillis();
        return "" + start + " Consumer received." +
            "\t" + result +
            "\r\n" + end + " Consumer Return";
    }

    @RequestMapping(value = "/consumer/alive", method = RequestMethod.GET)
    public boolean alive() {
        return true;
    }

    @RequestMapping(value = "/consumer-echo/feign/{str}", method = RequestMethod.GET)
    public String feign2(@PathVariable String str) {
        return echoService.echo(str, "") + " By feign.";
    }
}
