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
package io.cloudapp.api.gateway.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.util.function.Function;

public class CustomAuthorizer implements Authorizer {

    private final Function<HttpRequest, String> generate;
    private final String headerName;

    public CustomAuthorizer(Function<HttpRequest, String> generate, String headerName) {
        this.generate = generate;
        this.headerName = headerName;
    }

    public CustomAuthorizer(Function<HttpRequest, String> generate) {
        this.generate = generate;
        this.headerName = HttpHeaders.AUTHORIZATION;
    }

    @Override
    public void applyAuthorization(HttpRequest request) {
        request.getHeaders().add(headerName, generate.apply(request));
    }
}
