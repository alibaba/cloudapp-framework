# Transaction design and implement

# Description

Transaction management is based on [seata](https://seata.apache.org/zh-cn/) by design. Building on Seata's original 
capabilities, Seata's capabilities at the observable level are enhanced. Insert transactions into 
_**@GlobalTransactional**_ annotations in seata through facet programming, as well as integration with OpenTelemetry 
to collect relevant metrics for transactions in seata.

# Scenario

| **Feature**      | **Function/Case** | **TXC support** | **demo support**                 |
|:-----------------|:------------------|-----------------|----------------------------------|
| Transaction mode | -                 | -               | <input type="checkbox" checked>  |



# Dependencies

| **Components** | **SDK**                        | **Version** |
|:---------------|--------------------------------|-------------|
| txc implement  | txc-client                     | 2.8.95      |
|                | txc-common                     | 2.8.95      |
|                | txc-resourcemanager            | 2.8.95      |
|                | txc-rpc                        | 2.8.95      |
|                | txc-client-springcloud         | 2.8.95      |
|                | txc-datasource                 | 2.8.95      |
|                | txc-datasource-common          | 2.8.95      |
|                | txc-datasource-cobar           | 2.8.95      |
|                | druid-spring-boot-starter      | 1.2.23      |
|                | spring-boot-starter-data-jpa   | 2.7.18      |
|                | spring-cloud-starter-openfeign | 3.1.5       |

## maven repository
```xml
<repositories>
  <repository>
    <id>edas-repository</id>
    <name>EDAS Repository</name>
    <url>https://edas-public.oss-cn-hangzhou.aliyuncs.com/repository</url>
    <releases>
      <enabled>true</enabled>
      <checksumPolicy>fail</checksumPolicy>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```


# Abstract

See the `seata` package in the `cloudapp-base-api` module .


# Get Start

How do I start to implement framework interfaces for extension? The recommended process is as follows:

1. Dependency management <br>
   Unify the management of dependencies implemented by the framework in the dependency management module
   `cloudapp-framework-dependencies`;
2. Implementation
    1. Create framework implementation modules, and introduce the framework service unified abstraction module
       `cloudapp-base-api`, dependency management module `cloudapp-framework-dependencies`, and other dependencies
       that may be required in `pom.xml`.
    2. Implement interfaces, create implementation classes in framework implementation modules;
3. Starter integration <br>
   Introduce framework implementations in the framework starter module `spring-boot-starter-cloudapp`, and
   integrate their implementations into Starter, making it easier for applications to use directly through Spring
   Boot Starter. In the process of integration into Starter, it mainly realizes two characteristics: automatic
   configuration and dynamic configuration;
