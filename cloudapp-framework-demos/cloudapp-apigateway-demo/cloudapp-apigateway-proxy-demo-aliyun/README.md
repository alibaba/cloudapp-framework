# cloudapp-apigateway-proxy-demo-aliyun

## Description

Proxy request use cases.

## Before start

1. Create a gateway consumer with the APIKEY authentication type

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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
    </dependency>

</dependencies>
```

## Configuration

Application configuration example is as follows:

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        brokerUrl: ${brokerUrl}

apiKey:
  apiKey: acd5c2e14b8f6d83d00
  headerName: x-csb-apikey
```

## Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.