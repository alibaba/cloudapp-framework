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

package io.cloudapp.starter.mail.configuration;

import io.cloudapp.mail.ThymeleafTemplateEmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
@ConditionalOnClass({
        TemplateEngine.class,
        ThymeleafTemplateEmailService.class
})
public class ThymeleafMailAutoConfiguration {
    
    @Bean("thymeleafTemplateEmailService")
    @ConditionalOnMissingBean({
            ThymeleafTemplateEmailService.class,
    })
    public ThymeleafTemplateEmailService emailService(
            JavaMailSender javaMailSender,
            TemplateEngine templateEngine
    ) {
        return new ThymeleafTemplateEmailService(javaMailSender, templateEngine);
    }
}
