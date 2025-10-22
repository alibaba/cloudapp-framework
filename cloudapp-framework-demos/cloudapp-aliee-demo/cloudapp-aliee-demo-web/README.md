# cloudapp-aliee-demo-aliyun

## Description

Use aliee as the application server.

## Before start

1. Run a nacos server.


## Dependencies

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>com.alibaba.cloudapp</groupId>
      <artifactId>cloudapp-aliee-demo</artifactId>
      <version>${revision}</version>
      <relativePath>../pom.xml</relativePath>
   </parent>

   <artifactId>cloudapp-aliee-demo-web</artifactId>
   <version>${revision}</version>
   <packaging>jar</packaging>

   <dependencyManagement>
      <dependencies>
         <dependency>
            <groupId>com.alibaba.cloudapp</groupId>
            <artifactId>cloudapp-framework-dependencies</artifactId>
            <version>${revision}</version>
            <type>pom</type>
            <scope>import</scope>
         </dependency>
      </dependencies>
   </dependencyManagement>

   <dependencies>
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter</artifactId>
      </dependency>


      <dependency>
         <groupId>com.alibaba.cloudapp</groupId>
         <artifactId>spring-boot-starter-cloudapp</artifactId>
      </dependency>

      <!--    -->
      <dependency>
         <groupId>com.alibaba.cloudapp.microservice</groupId>
         <artifactId>cloudapp-microservice-springcloud-aliyun</artifactId>
      </dependency>

      <!--    AliEE -->
      <dependency>
         <groupId>com.alibaba.cloudapp</groupId>
         <artifactId>cloudapp-starter-aliee</artifactId>
      </dependency>


   </dependencies>

   <repositories>
      <repository>
         <id>aliee-repository</id>
         <name>EDAS AliEE Repository</name>
         <url>https://edas-public.oss-cn-hangzhou.aliyuncs.com/repository</url>
         <releases>
            <enabled>true</enabled>
         </releases>
         <snapshots>
            <enabled>true</enabled>
         </snapshots>
      </repository>
   </repositories>

</project>
```

## Configuration

Application configuration example is as follows:

```yaml
spring:
   application:
      name: aliee-demo
   cloud:
      prop: prop
      nacos:
         discovery:
            server-addr: 127.0.0.1:8848
         config:
            server-addr: 127.0.0.1:8848
            group: DEFAULT_GROUP
            import-check:
               enabled: false
            enabled: true
   config:
      import:
         - nacos:aliee-demo.yaml?refreshEnabled=true

server:
   port: 8083
```

## Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.