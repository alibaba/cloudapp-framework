# Cache design and implement

# Description

Distributed cache is designed based on redis, and in the framework design, on the one hand, it supports dynamic 
refresh and loading of cache configuration. On the other hand, cache management capabilities are enhanced for 
specific scenarios, such as encryption and decryption, which can support national secret algorithms, cache disks, 
and cache monitoring, such as discovering big keys.


# Scenario

| **Feature**     | **Function/Case**                       | **Redis support**               | **demo support**                   |
|:----------------|:----------------------------------------|---------------------------------|------------------------------------|
| Core            | automatic configuration                 | <input type="checkbox" checked> | <input type="checkbox" checked>    |
|                 | dynamic configuration                   | <input type="checkbox" checked> | -                                  |
| Data processing | encryption and decryption               | <input type="checkbox" checked> | <input type="checkbox" checked>    |
|                 | custom monitoring                       | <input type="checkbox" checked> | <input type="checkbox" checked>    |
|                 | custom disk cache                       | <input type="checkbox" checked> | <input type="checkbox" checked>    |


# Dependencies

| **Components**                                | **SDK**                 | **Version**      |
|:----------------------------------------------|-------------------------|------------------|
| spring data redis                             | spring-data-redis       | 2.7.18           |
| redis synchronization blocks the model client | jedis                   | 3.8.0            |
| redis asynchronous non-blocking model client  | lettuce-core            | 6.1.10.RELEASE   |
| algorithm implementation                      | bcprov-jdk18on          | 1.78             |


# Abstract

See the `cache` package in the `cloudapp-base-api` module .


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
