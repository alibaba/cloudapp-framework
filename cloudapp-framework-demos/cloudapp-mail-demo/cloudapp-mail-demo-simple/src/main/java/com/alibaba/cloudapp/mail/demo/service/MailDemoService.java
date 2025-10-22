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

package com.alibaba.cloudapp.mail.demo.service;

import com.alibaba.cloudapp.api.email.EmailService;
import com.alibaba.cloudapp.exeption.CloudAppException;
import com.alibaba.cloudapp.model.MailUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class MailDemoService {
    
    private static final Logger logger =
            LoggerFactory.getLogger(MailDemoService.class);
    
    @Autowired
    private EmailService mailService;
    @Value("${io.cloudapp.mail.username}")
    private String from;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    /**
     * read file from resources folder
     * @param fileName the name of file
     * @return file path
     * @throws IOException if read file error
     */
    public Path readFileFromResources(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        return resource.getFile().toPath();
    }
    
    public void sendMail(String to, String subject, String content) {
        try {
            MailUser mailUser = new MailUser()
                    .setTo(to)
                    .setFrom(from);
            
            mailService.sendEmail(mailUser, subject, content, false);
            
        } catch (CloudAppException e) {
            logger.error("send mail fail:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public void sendMailFile(String to, String subject, String content) {
        try {
            MailUser mailUser = new MailUser()
                    .setTo(to)
                    .setFrom(from);
            
            Path path = readFileFromResources("README.txt");
            
            mailService.sendEmail(mailUser, subject, content, path, false);
            
        } catch (CloudAppException e) {
            logger.error("send mail fail:" + e.getMessage());
            throw new RuntimeException(e);
            
        } catch (IOException e) {
            logger.error("Fail to read file:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
}
