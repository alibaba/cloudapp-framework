# OAuth2 design and implement

# Description

OAuth2 applications are designed based on the [OAuth2.0 specification](https://datatracker.ietf.org/doc/html/rfc6749) . 
The framework mainly abstracts common operations of tokens in OAuth2, including: acquisition, storage, and verification. 
The AuthorizationService is defined to handle the acquisition token, including the access token and the refresh token. 
Define a token storage service TokenStorageService is used to store acquired tokens, such as stored in application memory or redis middleware. 
At the same time, you define a login processor, a token validator, which handles login operations and validates tokens.


# Scenario

| **Feature**   | **Function/Case**       | **OAuth2 support**              | **demo support**                 |
|:--------------|:------------------------|---------------------------------|----------------------------------|
| Core          | automatic configuration | <input type="checkbox" checked> | <input type="checkbox" checked>  |
|               | dynamic configuration   | <input type="checkbox" checked> | <input type="checkbox" checked>  |
| Authorization | authorization           | <input type="checkbox" checked> | <input type="checkbox" checked>  |
|               | token validation        | <input type="checkbox" checked> | <input type="checkbox" checked>  |
|               | token storage           | <input type="checkbox" checked> | <input type="checkbox" checked>  |
|               | login filter            | <input type="checkbox" checked> | <input type="checkbox" checked>  |


# Dependencies

| **Components**     | **SDK**    | **Version** |
|:-------------------|------------|-------------|
| jwt implementation | java-jwt   | 4.4.0       |
|                    | jwks-rsa   | 0.22.1      |


# Abstract

See the `oauth2` package in the `cloudapp-base-api` module .


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
