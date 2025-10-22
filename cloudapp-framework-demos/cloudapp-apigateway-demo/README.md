# cloudapp-apigateway-demo

# Description

Use cases implemented by the gateway service.

# Modules

## cloudapp-apigateway-apikey-demo-aliyun

APIKey use cases.

### Before start

1. Create a gateway consumer with the APIKEY authentication type

### Dependencies

```xml
<dependencies>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
   </dependency>

   <dependency>
      <groupId>com.alibaba.cloudapp</groupId>
      <artifactId>spring-boot-starter-cloudapp</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>com.alibaba.cloudapp</groupId>
      <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
   </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
   application:
      name: apigateway-aliyun-demo
   main:
      allow-bean-definition-overriding: true

io:
   cloudapp:
      apigateway:
         aliyun:
            enabled: true
            brokerUrl: ${brokerUrl:}
            apiKey:
               apiKey: ${apiKey:}
               headerName: ${headerName:x-csb-apikey}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.

## cloudapp-apigateway-basic-demo-aliyun

Basic authentication use cases.

### Before start

1. Create a gateway consumer with the basic authentication type


### Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>spring-boot-starter-cloudapp</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
    </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true
io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        brokerUrl: ${brokerUrl}
        basic:
          username: ${username}
          password: ${password}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


## cloudapp-apigateway-jwt-demo-aliyun

JWT authentication use cases.

### Before start

1. Create a gateway consumer with the JWT authentication type


### Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>spring-boot-starter-cloudapp</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
    </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        brokerUrl: ${brokerUrl}
        jwt:
          keyId: ${keyId}
          secret: ${secret}
          issuer: ${issuer:}
          subject: ${subject:}
          audience: ${audience:}
          expiredSecond: ${expiredSecond:31536000}
          algorithm: ${algorithm:HS256}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


## cloudapp-apigateway-manager-demo-aliyun

Gateway management use cases, including create consumer.

### Before start

1. Apply for AK and SK on the AccessKey management page and set accessKey and secretKey to the environment variables. or Replace the accessKey and secretKey in application.yml


### Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>spring-boot-starter-cloudapp</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
    </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        server:
          accessKey: ${AK}
          secretKey: ${SK}
#          The gateway product controls the link OpenAPI endpoint
          gatewayUri: ${openApiUrl:csb-cop-api-biz.inter.env205.shuguang.com}
          instanceId: ${instanceId:i-acd5c2e14bdfd2cc000}
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


## cloudapp-apigateway-oauth2-demo-aliyun

OAuth2 use cases.

### Before start

1. Create a gateway consumer with the OAuth2 authentication type


### Dependencies

```xml
<dependencies>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
   </dependency>

   <dependency>
      <groupId>com.alibaba.cloudapp</groupId>
      <artifactId>spring-boot-starter-cloudapp</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>com.alibaba.cloudapp</groupId>
      <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
   </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        oAuth2:
          client-id: ${clientId}
          client-secret: ${clientSecret}
          redirect-uri: http://localhost:8080/oauth/code
          scopes:
            - openid
            - profile
          grant-types:
            - authorization_code
            - refresh_token
          authorization-uri: https://gitlab.com/oauth/authorize
          token-uri: https://gitlab.com/oauth/token
          jwks-url: https://gitlab.com/oauth/discovery/keys
          filter-skip-urls:
            - /manager/**
            - /proxy/**
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.

## cloudapp-apigateway-proxy-demo-aliyun

Proxy request use cases.

### Before start

1. Create a gateway consumer with the APIKEY authentication type

### Dependencies

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>spring-boot-starter-cloudapp</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloudapp</groupId>
        <artifactId>cloudapp-spring-apigateway-aliyun</artifactId>
    </dependency>

</dependencies>
```

### Configuration

```yaml
spring:
  application:
    name: apigateway-aliyun-demo
  main:
    allow-bean-definition-overriding: true

io:
  cloudapp:
    apigateway:
      aliyun:
        enabled: true
        brokerUrl: ${brokerUrl}

apiKey:
  apiKey: acd5c2e14b8f6d83d00
  headerName: x-csb-apikey
```

### Run demo

Open the main class of the Spring Boot application, configure the relevant envs and run it.


# Configuration reference

1.  A class of configuration parameters that defines the authentication implementation of the CSB service consumer _**ApiGatewayProperties**_ , use annotations _**@ConfigurationProperties(prefix = "io.cloudapp.apigateway.aliyun")**_ , properties
    as follows:


| **Key** | **Data Type**        | **Default** | **Remark**                              |
|---------|----------------------|-------------|-----------------------------------------|
| apiKey  | **ApiKeyProperties** | \-          | APIKey authentication parameter class   |
| jwt     | **JwtProperties**    | \-          | JWT authentication parameter class      |
| oAuth2  | **BasicProperties**  | \-          | Basic authentication parameter class    |
| basic   | **OAuth2Properties** | \-          | OAuth2 authentication parameter class   |

 _**ApiKeyProperties**_ , properties as follows:


| **Key**    | **Data Type**          | **Default**   | **Remark**          |
|------------|------------------------|---------------|---------------------|
| apiKey     | String                 | \-            | apikey              |
| headerName | String                 | \-            | request header name |

 _**JwtProperties**_ , properties as follows:


| **Key**            | **Data Type** | **Default** | **Remark**                        |
|--------------------|---------------|-------------|-----------------------------------|
| keyId              | String        | \-          | KeyId                             |
| secret             | String        | \-          | secret                            |
| issuer             | String        | \-          | issuer                            |
| subject            | String        | \-          | subject                           |
| audience           | String        | \-          | audience                          |
| expiredSecond      | long          | \-          | expiration time, in seconds       |
| base64EncodeSecret | boolean       | false       | whether the key is base64 encoded |
| algorithm          | String        | HS256       | signature algorithm               |

_**BasicProperties**_ , properties as follows:


| **Key**  | **Data Type** | **Default** | **Remark**  |
|----------|---------------|-------------|-------------|
| username | String        | \-          | username    |
| password | String        | \-          | password    |

_**OAuth2Properties**_ , properties as follows:


| **Key**          | **Data Type** | **Default** | **Remark**                                                                                                                       |
|------------------|---------------|-------------|----------------------------------------------------------------------------------------------------------------------------------|
| scopes           | List<String>  | \-          | Client (i.e., third-party app) permission scope                                                                                  |
| clientId         | String        | \-          | clientID                                                                                                                         |
| clientSecret     | String        | \-          | clientSecret                                                                                                                     |
| redirectUri      | String        | \-          | redirectUri                                                                                                                      |
| enablePkce       | boolean       | \-          | whether to enable Authorization Code Exchange Attestation Key (PKCE)                                                             |
| tokenUri         | String        | \-          | The authentication server is used to process the address of the authentication request (get the token, refresh the token, etc.); |
| authorizationUri | String        | \-          | The address where you got the authorization code                                                                                 |

2.  Define the configuration parameter class for the CSB gateway service _**ApiGatewayManagerProperties**_ , use annotations _**@ConfigurationProperties(prefix = "io.cloudapp.apigateway.aliyun.server")**_ , properties as follows:


| **Key**    | **Data Type** | **Default** | **Remark**           |
|------------|---------------|-------------|----------------------|
| accessKey  | String        | \-          | AccessKey ID         |
| secretKey  | String        | \-          | AccessKey Secret     |
| gatewayUri | String        | \-          | CSB openapi endpoint |
