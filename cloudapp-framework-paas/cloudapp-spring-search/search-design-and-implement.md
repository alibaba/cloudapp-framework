# Search design and implement

# Description

Distributed search is designed with reference to Elasticsearch, which defines common operations for common searches 
in the framework, such as search, insert, bulk insert, modify, and delete. In scenarios where you need to use search 
advanced use cases, you can use it by obtaining native client delegated instances.


# Scenario

| **Feature**                 | **Function/Case**               | **Alibaba Cloud Elasticsearch support**  | **demo support**                  |
|:----------------------------|:--------------------------------|------------------------------------------|-----------------------------------|
| Core                        | automatic configuration         | <input type="checkbox" checked>          | <input type="checkbox" checked>   |
|                             | dynamic configuration           | <input type="checkbox" checked>          | -                                 |
| Data management             | Search by mode/request          | <input type="checkbox" checked>          | <input type="checkbox" checked>   |
|                             | Create/update/delete documents  | <input type="checkbox" checked>          | <input type="checkbox" checked>   |
|                             | Create documents in bulk        | <input type="checkbox" checked>          | <input type="checkbox" checked>   |
| Cluster management          | -                               | -                                        | -                                 |
| Secure and highly available | -                               | -                                        | -                                 |


# Dependencies

| **Components**       | **SDK**              | **Version**    |
|:---------------------|----------------------|----------------|
| elasticsearch client | elasticsearch-java   | 8.15.0         | 


# Abstract

See the `search` package in the `cloudapp-base-api` module .


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
