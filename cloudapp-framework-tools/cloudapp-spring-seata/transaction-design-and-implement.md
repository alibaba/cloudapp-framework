# Transaction design and implement

# Description

Transaction management is based on [seata](https://seata.apache.org/zh-cn/) by design. Building on Seata's original 
capabilities, Seata's capabilities at the observable level are enhanced. Insert transactions into 
_**@GlobalTransactional**_ annotations in seata through facet programming, as well as integration with OpenTelemetry 
to collect relevant metrics for transactions in seata.

# Scenario

| **Feature**                | **Function/Case**       | **Seata support**                 | **demo support**                 |
|:---------------------------|:------------------------|-----------------------------------|----------------------------------|
| Core                       | automatic configuration | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
|                            | dynamic configuration   | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
| OpenTelemetry monitor      | -                       | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
| transactional patterns     | AT mode                 | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
|                            | TCC mode                | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
|                            | Saga mode               | <input type="checkbox" checked>   | <input type="checkbox" checked>  |
|                            | XA mode                 | <input type="checkbox" checked>   | <input type="checkbox" checked>  |


# Dependencies

| **Components**        | **SDK**                                   | **Version**  |
|:----------------------|-------------------------------------------|--------------|
| seata implementation  | spring-cloud-starter-alibaba-seata        | 2.2.10       |
| OpenTelemetry         | opentelemetry-instrumentation-annotations | 2.6.0        |


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
