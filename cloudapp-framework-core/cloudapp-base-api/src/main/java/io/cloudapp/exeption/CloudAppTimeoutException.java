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

package io.cloudapp.exeption;

/**
 * Typically, this exception is represents for a client reading or sending
 * via a socket channel, like socket timeout or uploading a big file to the
 * server.
 */
public class CloudAppTimeoutException extends CloudAppException {
    private static final String CODE = "CloudApp.Timeout";
    
    public CloudAppTimeoutException(String msg, String code) {
        super(msg, code);
    }
    
    public CloudAppTimeoutException(String msg, String code, Throwable cause) {
        super(msg, code, cause);
    }
    
    public CloudAppTimeoutException(String msg, Throwable cause) {
        super(msg, CODE, cause);
    }
    
}
