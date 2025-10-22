# cloudapp-config-demo

# Description

Configure services and configure management use cases.

# Modules

## cloudapp-config-demo-aliyun

Configure management and configuration of service use cases.

### Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and secretKey to the environment variables. or
   Replace the accessKey and secretKey in application.yml

2. Create a microservice space and group in the Alibaba Cloud EDAS console.

3. Run a nacos server


### Dependencies

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
      <artifactId>cloudapp-spring-config-aliyun</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
   application:
      name: config-aliyun-demo
   cloud:
      nacos:
         config:
            server-addr: 127.0.0.1:8848
            import-check:
               enabled: false

io:
   cloudapp:
      config:
         aliyun:
            read:
               enabled: true
               timeout: 5000
               group: cloudapp
            write:
               enabled: true
               timeout: 5000
               group: cloudapp
               regionId: cn-hangzhou
               namespaceId: 3a05b6c2-4f93-474d-9856-6e7b75d20296
               domain: acm.cn-hangzhou.aliyuncs.com
               accesskey: ${AK}
               secretKey: ${SK}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.

## cloudapp-config-read-demo-aliyun

Get config use cases.

### Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and secretKey to the environment variables. or
   get username and password for nacos.

2. Run a nacos server

### Dependencies

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
      <artifactId>cloudapp-spring-config-aliyun</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
   application:
      name: config-read-aliyun-demo
   cloud:
      nacos:
         config:
            server-addr: ${NACOS_SERVER_ADDR:127.0.0.1:8848}
            import-check:
               enabled: false
            namespace: ${NACOS_NAMESPACE:}
            access-key: ${AK:}
            secret-key: ${SK:}

io:
   cloudapp:
      config:
         aliyun:
            read:
               enabled: true
               timeout: 5000
               group: cloudapp
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.

## cloudapp-config-manager-demo-aliyun

Configuration management use cases.

### Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and secretKey to the environment variables. or
   Replace the accessKey and secretKey in application.yml

2. Create a microservice space and group in the Alibaba Cloud EDAS console.


### Dependencies

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
      <artifactId>cloudapp-spring-config-aliyun</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
   application:
      name: config-manager-aliyun-demo
   cloud:
      nacos:
         config:
            import-check:
               enabled: false

io:
   cloudapp:
      config:
         aliyun:
            write:
               enabled: true
               timeout: 5000
               group: ${group:cloudapp}
               regionId: ${regionId:any}
               namespaceId: ${namespaceId}
               domain: ${dncs_endpoint}
               accessKey: ${AK}
               secretKey: ${SK}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


# Configuration reference

1. Define the configuration parameter class that gets the nacos configuration service implementation _**NacosConfigReadServiceProperties**_ , use annotations _**@ConfigurationProperties(prefix="io.cloudapp.config.aliyun.read")**_ , properties as follows:


| **Key** | **Data Type** | **Default**    | **Remark**                   |
|---------|---------------|----------------|------------------------------|
| enabled | boolean       | \-             | whether enabled or not       |
| group   | String        | DEFAULT\_GROUP | group                        |
| timeout | int           | 3000           | timeout time in milliseconds |

For other configuration properties, see the fields of the _**com.alibaba.cloud.nacos.NacosConfigProperties**_ class.

2. Define the configuration parameter class implemented by Alibaba Cloud Configuration Manager _**AliyunConfigManagerProperties**_ , use annotations _**@ConfigurationProperties(prefix="io.cloudapp.config.aliyun.write")**_ , properties as follows:


| **Key**     | **Data Type** | **Default**    | **Remark**                                                          |
|-------------|---------------|----------------|---------------------------------------------------------------------|
| enabled     | boolean       | \-             | Whether Alibaba Cloud Configuration Manager is enabled              |
| group       | String        | DEFAULT\_GROUP | group                                                               |
| timeout     | int           | 3000           | timeout time in milliseconds                                        |
| domain      | String        | \-             | configuration center address, example: acm.cn-hangzhou.aliyuncs.com |
| regionId    | String        | \-             | region, example:cn-hangzhou                                         |
| protocol    | String        | http           | protocol, Optional:http/https                                       |
| accessKey   | String        | \-             | AccessKey ID                                                        |
| secretKey   | String        | \-             | AccessKey Secret                                                    |
| namespaceId | String        | \-             | namespaceId                                                         |

3. Define the configuration parameter class for the CSB gateway service _**ApiGatewayManagerProperties**_ , use annotations _**@ConfigurationProperties(prefix = "io.cloudapp.apigateway.aliyun.server")**_ , properties as follows:


| **Key**    | **Data Type** | **Default** | **Remark**           |
|------------|---------------|-------------|----------------------|
| accessKey  | String        | \-          | AccessKey ID         |
| secretKey  | String        | \-          | AccessKey Secret     |
| gatewayUri | String        | \-          | CSB openapi endpoint |
