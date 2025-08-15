# Microservice design and implement

# Description

The main reference for microservice design is [Canary Release]( https://help.aliyun.com/zh/edas/user-guide/use-the-edas-console-to-implement-canary-releases-of-applications-in-kubernetes-clusters-2) 
and [Full-link lane grayscale release](https://help.aliyun.com/zh/edas/user-guide/full-link-canary-grayscale-release-using-full-link-swimlanes-k8s), Perform the grayscale 
release process with the configured grayscale policy. The framework provides an Alibaba version implementation that 
defines a common combination of dependencies for microservices: service registration, service discovery, web support,
and HTTP client, which automatically introduce the above dependencies when used without manual management.


# Scenario

| **Feature**             | **Function/Case**                             | **Alibaba Cloud EDAS support**  | **demo support**                |
|:------------------------|:----------------------------------------------|---------------------------------|---------------------------------|
| Core                    | automatic configuration                       | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | dynamic configuration                         | <input type="checkbox" checked> | <input type="checkbox" checked> |
| Microservice Management | Check if microservice management is supported | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | traffic marking                               | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | get traffic labels                            | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | check the traffic label                       | -                               | -                               |
|                         | get the environment label                     | -                               | -                               |
|                         | set up user request data                      | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | get user request data                         | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                         | check if it's being released on canary        | -                               | -                               |
|                         | check if the release is being warmed up       | -                               | -                               |
|                         | get traceId                                   | <input type="checkbox" checked> | <input type="checkbox" checked> |


# Dependencies

| **Components**                        | **SDK**                                      | **Version** |
|:--------------------------------------|----------------------------------------------|-------------|
| spring cloud alibaba nacos config     | spring-cloud-starter-alibaba-nacos-config    | 2021.0.5.0  |
| spring cloud alibaba nacos discovery | spring-cloud-starter-alibaba-nacos-discovery | 2021.0.5.0  |
| OpenTelemetry API                     | opentelemetry-api                            | 1.43.0      |
| OpenTelemetry context                 | opentelemetry-context                        | 1.43.0      |
| spring boot dubbo                     | dubbo-spring-boot-starter                    | 2.7.13      |


# Abstract

See the `microservice` package in the `cloudapp-base-api` module .


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
