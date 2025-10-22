# Datasource design and implement

# Description

The data source is designed based on [Apache Druid](https://druid.apache.org/). The data source implementation 
inherits from the `DruidDataSourceWrapper`, continuing the original ability of `druid-spring-boot-starter`, and adding 
the ability to dynamically refresh data sources, such as `spring.datasource.username`, `spring.datasource.password`, 
`spring.datasource.url`, and `spring.datasource.driver-class-name` to dynamically refresh the data source Druid 
property, which can be used to gracefully close the connection and connect to a new address when the database connection changes.


# Scenario

| **Feature**           | **Function/Case**           | **Druid support**                  | **demo support**                  |
|:----------------------|:----------------------------|------------------------------------|-----------------------------------|
| Core                  | automatic configuration     | <input type="checkbox" checked>    | <input type="checkbox" checked>   |
|                       | dynamic configuration       | <input type="checkbox" checked>    | -                                 |
| DataSource management | observable                  | <input type="checkbox" checked>    | <input type="checkbox" checked>   |


# Dependencies

| **Components**    | **SDK**                    | **Version** |
|:------------------|----------------------------|-------------|
| spring boot druid | druid-spring-boot-starter  | 1.2.23      |


# Abstract

See the `datasource` package in the `spring-boot-starter-cloudapp` module .


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
