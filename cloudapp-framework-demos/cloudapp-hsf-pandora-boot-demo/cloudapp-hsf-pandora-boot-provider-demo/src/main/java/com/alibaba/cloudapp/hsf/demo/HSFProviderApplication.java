package com.alibaba.cloudapp.hsf.demo;

import com.taobao.pandora.boot.PandoraBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// run command: java -Djmenv.tbsite.net=127.0.0.1 -Dpandora.location=${USER_HOME}/.m2/repository/com/taobao/pandora/taobao-hsf.sar/2019-06-stable/taobao-hsf.sar-2019-06-stable.jar -jar hsf-pandora-boot-provider-1.0.jar
@SpringBootApplication
public class HSFProviderApplication {

    public static void main(String[] args) {
        PandoraBootstrap.run(args);
        SpringApplication.run(HSFProviderApplication.class, args);
        PandoraBootstrap.markStartupAndWait();
    }
}
