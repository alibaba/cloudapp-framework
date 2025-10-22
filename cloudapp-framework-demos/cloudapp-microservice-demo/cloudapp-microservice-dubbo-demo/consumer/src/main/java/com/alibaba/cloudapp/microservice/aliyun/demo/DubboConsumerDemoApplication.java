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

package com.alibaba.cloudapp.microservice.aliyun.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DubboConsumerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerDemoApplication.class, args);
    }

    @RestController
    public class ConsumerController {
        @Reference(check = false, version = "1.0.0", group = "DUBBO")
        private DemoService demoService;

        @Reference(check = false, version = "1.0.0", group = "DUBBO")
        private ItemService itemService;

        @GetMapping("/{tag}")
        public String tag(HttpServletRequest request,
                          @PathVariable String tag) {
            return demoService.tag(tag);
        }

        @GetMapping("/hello")
        public String sayHello(@RequestParam(required = false) String name) {
            return demoService.sayHello(name);
        }

        @GetMapping("/user/{userId}")
        public Map<String, Object> getUserInfo(@PathVariable Long userId) {
            return demoService.getUserInfo(userId);
        }

        @PostMapping("/users")
        public List<Map<String, Object>> getUserList(@RequestBody List<Long> userIds) {
            return demoService.getUserList(userIds);
        }

        @GetMapping("/echo")
        public String echo(@RequestParam String message) {
            return demoService.echo(message);
        }

        @GetMapping("/item/{itemId}")
        public Map<String, Object> getItem(@PathVariable Long itemId) {
            return itemService.getItem(itemId);
        }

        @GetMapping("/items/category/{category}")
        public List<Map<String, Object>> getItemsByCategory(@PathVariable String category) {
            return itemService.getItemsByCategory(category);
        }

        @GetMapping("/items/search")
        public List<Map<String, Object>> searchItems(@RequestParam String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
            return itemService.searchItems(keyword, page, size);
        }

        @GetMapping("/item/{itemId}/stock")
        public int checkStock(@PathVariable Long itemId) {
            return itemService.checkStock(itemId);
        }

        @GetMapping("/health-check")
        public String healthCheck() {
            return "Consumer OK";
        }
    }
}
