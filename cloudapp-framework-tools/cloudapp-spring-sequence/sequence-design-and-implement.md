# Sequence design and implement

# Description

The global sequence mainly generates a globally unique ID for each object in the distributed system, and the 
framework supports the unique ID generator by default, and there are two common algorithms, the first is to generate the 
algorithm locally: such as [Snowflake Algorithm](https://github.com/twitter-archive/snowflake); Another 
implementation is to rely on a distributed service such as Redis. In the design of Cloud Apps, both implementations 
are provided by default.


# Scenario

| **Feature** | **Function/Case**                     | **support**                        | **demo support**                 |
|:------------|:--------------------------------------|------------------------------------|----------------------------------|
| Core        | automatic configuration               | <input type="checkbox" checked>    | <input type="checkbox" checked>  |
|             | dynamic configuration                 | <input type="checkbox" checked>    | <input type="checkbox" checked>  |
| Sequence    | sequence based on redis               | <input type="checkbox" checked>    | <input type="checkbox" checked>  |
|             | sequence based on snowflake algorithm | <input type="checkbox" checked>    | <input type="checkbox" checked>  |


# Dependencies

| **Components**    | **SDK**           | **Version** |
|:------------------|-------------------|-------------|
| spring data redis | spring-data-redis | 2.7.18      |


# Abstract

See the `sequence` package in the `cloudapp-base-api` module .


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
