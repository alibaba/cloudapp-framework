# cloudapp-base-demo

# Description

Utils and threadpool use cases.

# Modules

## cloudapp-base-utils-demo

Utils use cases.

### Before start

No preconditions.

### Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-base-utils</artifactId>
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
    name: UtilsDemo

server:
  port: 8080
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.

## cloudapp-threadpool-demo

Threadpool use cases, including automatic configuration and dynamic configuration.

### Before start

1. Run a nacos server

### Dependencies

```xml
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloudapp</groupId>
            <artifactId>spring-boot-starter-cloudapp</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloudapp</groupId>
            <artifactId>cloudapp-base-api</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

    </dependencies>
```

### Configuration

```yaml
io:
  cloudapp:
    base:
      threadpool:
        enabled: true
        corePoolSize: 6
        maximumPoolSize: 9

spring:
  application:
    name: thread-pool-demo
  config:
    import: nacos:thread-pool-demo.yaml?refresh=true
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        username: nacos
        password: nacos
        group: DEFAULT_GROUP
server:
  port: 18080
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


# Configuration reference

## Threadpool

1.  Defines the configuration class for the threadpool implementation _**ThreadPoolProperties**_ , inherited from _**RefreshableProperties**_ , use annotations _**@ConfigurationProperties(prefix = ThreadPoolProperties.BINDING\_PROP\_KEY)**_ , and _**ThreadPoolProperties.BINDING\_PROP\_KEY=“io.cloudapp.base.threadpool”**_ , properties as follows:


| **Key**                 | **Data Type**         | **Default**  | **Remark**                                                                                                                                                                                    |
|-------------------------|-----------------------|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| enabled                 | boolean               | \-           | whether enabled or not                                                                                                                                                                        |
| corePoolSize            | int                   | 2            | the number of core thread pools, which is the minimum number of threads to remain active                                                                                                      |
| maximumPoolSize         | int                   | 10           | maximum number of thread pools                                                                                                                                                                |
| keepAliveSeconds        | int                   | 5            | timeout (in seconds) for idle threads waiting for work. When the number of threads present exceeds the corePoolSize, the threads will use this timeout value; Otherwise it will keep waiting. |
| queueCapacity           | int                   | 100          | queue size                                                                                                                                                                                    |
| threadNamePrefix        | String                | Base         | thread name prefix                                                                                                                                                                            |
| awaitTerminationSeconds | long                  | 1            | waiting time in seconds                                                                                                                                                                       |
| allowCoreThreadTimeOut  | boolean               | true         | allow core threads to time out                                                                                                                                                                |


## Utils

No configuration properties.