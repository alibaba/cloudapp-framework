# Config design and implement

# Description

The distributed configuration is designed based on Nacos, and the abstraction layer divides the main interfaces into 
two categories, one is the interface with the suffix Service, which is more used for service links and is provided 
for developers to use in business processing. The other type is the interface with the manager suffix, which is more 
used to manage the management link and manage distributed configuration services rather than business processing. In 
a common scenario, the Framework defines configuration management interfaces and configuration service interfaces 
for managing configurations and reading listening configurations. In complex use case scenarios, Framework provides 
a method to obtain the underlying delegated instance for distributed configuration, and invoke the native ability of 
distributed configuration through the delegate instance.

# Scenario

| **Feature**         | **Function/Case**       | **Alibaba Cloud ACM support**   | **ACM demo support**            | **Nacos support**               | **Nacos demo support**          |
|:--------------------|:------------------------|---------------------------------|---------------------------------|---------------------------------|---------------------------------|
| Core                | automatic configuration | <input type="checkbox" checked> | <input type="checkbox" checked> | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                     | dynamic configuration   | <input type="checkbox" checked> | <input type="checkbox" checked> | <input type="checkbox" checked> | <input type="checkbox" checked> |
| Config Manager      | publish/delete config   | <input type="checkbox" checked> | <input type="checkbox" checked> | -                               | -                               |
|                     | publish/delete configs  | <input type="checkbox" checked> | <input type="checkbox" checked> | -                               | -                               |
| Config Read Service | read config             | -                               | -                               | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                     | listen config           | -                               | -                               | <input type="checkbox" checked> | <input type="checkbox" checked> |


# Dependencies


| **Components**                    | **SDK**                                   | **Version**  |
|:----------------------------------|-------------------------------------------|--------------|
| ACM implementation                | aliyun-java-sdk-acm                       | 1.0.1        |
| Alibaba Cloud core                | aliyun-java-sdk-core                      | 4.4.9        |
| Nacos                             | nacos-client                              | 2.4.0        |
| Spring Cloud Alibaba Nacos Config | spring-cloud-starter-alibaba-nacos-config | 2021.0.5.0   |


# Abstract

See the `config` package in the `cloudapp-base-api` module .


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
