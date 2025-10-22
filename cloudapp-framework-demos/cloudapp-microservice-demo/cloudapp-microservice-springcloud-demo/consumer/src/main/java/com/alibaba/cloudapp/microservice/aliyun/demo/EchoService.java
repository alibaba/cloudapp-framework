/**
 * Copyright (c) 2022-present Alibaba Group
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.alibaba.cloudapp.microservice.aliyun.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "spring-cloud-provider")
public interface EchoService {

    @RequestMapping(value = "/echo/{str}",method = RequestMethod.GET)
    String echo(@PathVariable("str") String str, @RequestParam("env") String env);

    @RequestMapping(value = "/tag2",method = RequestMethod.GET)
    String tag2();

}
