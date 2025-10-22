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

package com.alibaba.cloudapp.filestore.aliyun.util;

import com.alibaba.cloudapp.exeption.*;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.alibaba.cloudapp.exeption.*;

/**
 * Convert any exception to CloudAppException
 */
public class OSSExceptionConverter {

    public static CloudAppException convert(ClientException e) {
        String code = e.getErrorCode();
        return new CloudAppException(e.getMessage(), code, e);
    }

    public static CloudAppException convert(CloudAppException e) {
        return e;
    }

    public static CloudAppException convert(OSSException ex) {
        if (OSSErrorCode.REQUEST_TIMEOUT.equals(ex.getErrorCode())) {
            return new CloudAppTimeoutException(ex.getMessage(), ex.getErrorCode(), ex);
        }

        if (OSSErrorCode.ACCESS_DENIED.equals(ex.getErrorCode())) {
            return new CloudAppPermissionException(ex.getMessage(), ex.getErrorCode(), ex);
        }

        if(OSSErrorCode.INVALID_ACCESS_KEY_ID.equals(ex.getErrorCode()) ||
                OSSErrorCode.SIGNATURE_DOES_NOT_MATCH.equals(ex.getErrorCode())) {
            return new CloudAppInvalidAccessException(ex.getMessage(), ex.getErrorCode(), ex);
        }

        if (ex.getErrorCode() != null && (ex.getErrorCode().startsWith("NoSuch") ||
                ex.getErrorCode().startsWith("Invalid") ||
                ex.getErrorCode().startsWith("Missing") ||
                ex.getErrorCode().indexOf("Exist") > 0 ||
                ex.getErrorCode().indexOf("Empty") > 0 ||
                ex.getErrorCode().indexOf("Too") > 0
        )) {
            return new CloudAppInvalidRequestException(ex.getMessage(),
                    "CloudApp.BucketNotExisted", ex);
        }

        return new CloudAppException(ex.getMessage(), ex.getErrorCode(), ex);
    }

    public static CloudAppException convert(Throwable exception) {
        if (exception.getCause() instanceof OSSException
                || exception.getCause() instanceof ClientException
                || exception.getCause() instanceof CloudAppException) {
            return convert(exception.getCause());
        }
        return new CloudAppException(exception.getMessage(), exception);
    }

}
