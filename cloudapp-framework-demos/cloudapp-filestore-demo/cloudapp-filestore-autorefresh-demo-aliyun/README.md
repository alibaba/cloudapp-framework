# cloudapp-filestore-autorefresh-demo-aliyun

## Description

Automatic configuration and dynamic configuration.

## Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and
   secretKey to the environment variables. or Replace the accessKey and secretKey
   in application.yml

2. Create a bucket
3. Run a nacos server


## Dependencies

```xml
    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloudapp</groupId>
            <artifactId>spring-boot-starter-cloudapp</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloudapp</groupId>
            <artifactId>cloudapp-spring-filestore-aliyun</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

    </dependencies>
```

## Configuration

Application configuration example is as follows:

```yaml
io:
   cloudapp:
      filestore:
         aliyun:
            enabled: true
            access-key: ${AK}
            secret-key: ${SK}
            endpoint: http://oss-cn-shenzhen.aliyuncs.com

spring:
   application:
      name: oss-autorefresh-demo
   config:
      import: nacos:oss-autorefresh-demo.yaml?refresh=true
   cloud:
      nacos:
         config:
            server-addr: 127.0.0.1:8848
            username: nacos
            password: nacos
            group: DEFAULT_GROUP
server:
   port: 8080
```

## Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.