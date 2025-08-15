# Mail design and implement

# Description

The email part is abstracted based on JavaMail, simplifying the method of sending emails, and providing the ability 
to send customized emails through templates (Freemarker, Thymeleaf).


# Scenario

| **Feature** | **Function/Case**        | **JavaMail support**             | **demo support**                |
|:------------|:-------------------------|----------------------------------|---------------------------------|
| Core        | automatic configuration  | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|             | dynamic configuration    | <input type="checkbox" checked>  | -                               |
| Mail        | Plain text mail          | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|             | freemarker template mail | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|             | thymeleaf template mail  | <input type="checkbox" checked>  | <input type="checkbox" checked> |


# Dependencies

| **Components** | **SDK**                    | **Version**     |
|:---------------|----------------------------|-----------------|
| java mail      | jakarta.mail               | 1.6.7           |
| thymeleaf      | thymeleaf-spring5          | 3.0.15.RELEASE  |
|                | thymeleaf-extras-java8time | 3.0.4.RELEASE   |
| freemarker     | freemarker                 | 2.3.32          |


# Abstract

See the `email` package in the `cloudapp-base-api` module .


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
