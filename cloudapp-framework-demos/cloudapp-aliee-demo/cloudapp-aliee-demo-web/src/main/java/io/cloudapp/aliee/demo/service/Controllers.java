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
package io.cloudapp.aliee.demo.service;

import io.cloudapp.aliee.demo.service.interfaces.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class Controllers {

    @Value("${spring.cloud.prop}")
    private String prop;

    @Autowired
    private RemoteService remoteService;

    @GetMapping("/rpc")
    public String rpc() {
        return "RPC: ===>" + remoteService.echo("hello-world");
    }

    @GetMapping("/call")
    public String call() {
        return "call: " + prop;
    }

    @GetMapping("/echo/{msg}")
    public String echo(@PathVariable String msg) {
        return "echo: " + msg;
    }
}
