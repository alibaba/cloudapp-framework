# Gateway design and implement

# Description

A service gateway is essentially a service that handles HTTP requests and handles various types of HTTP requests as a unified entry and exit. In the framework, the authentication processing interface is defined, and in common authentication type scenarios such as Basic, API Key, JWT, and OAuth2, the framework defines the authentication processing classes corresponding to various authentication types. In scenarios where there is special logic or you want to customize the authentication logic, the corresponding custom authentication processing class is also provided.

There are two main working scenarios for service gateways: general service gateways and proxy gateways, and the framework defines corresponding abstract classes. The Universal Services Gateway can be used as a standard service to process requests and generate responses. The proxy gateway acts as a proxy that forwards the received request to other services and returns the service response.


# Scenario

| **Feature**             | **Function/Case**                      | **CSB2 support**                  | **demo support**                  |
|:------------------------|:---------------------------------------|-----------------------------------|-----------------------------------|
| Core                    | automatic configuration                | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | dynamic configuration                  | <input type="checkbox" checked>   | -                                 |
| Consumer authentication | APIKey                                 | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | Basic                                  | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | JWT                                    | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | OAuth2                                 | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | Custom authentication                  | <input type="checkbox" checked>   | -                                 |
| Request                 | get/asynGet                            | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | post/asynPost                          | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | Custom requests                        | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
|                         | Proxy request                          | <input type="checkbox" checked>   | <input type="checkbox" checked>   |
| Service integration     | Integrated registry services           | -                                 | -                                 |
|                         | Integrate fixed-address services       | -                                 | -                                 |
| API management          | create/update/debug/offline API        | -                                 | -                                 |
| Gateway routing         | Expose integrated services to consumer | -                                 | -                                 |
| Plugin management       | -                                      | -                                 | -                                 |
| Flow control            | -                                      | -                                 | -                                 |
| Gateway cascade         | -                                      | -                                 | -                                 |
| Logs                    | -                                      | -                                 | -                                 |


# Dependencies

| **Components**                         | **SDK**                | **Version**                           |
|:---------------------------------------|------------------------|---------------------------------------|
| Alibaba Cloud core                     | aliyun-java-sdk-core   | 4.4.9                                 |
| OAuth2 implementation in the framework | cloudapp-spring-oauth2 | Consistent with the framework version |


# Abstract

See the `gateway` package in the `cloudapp-base-api` module .


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
