# cloudapp-microservice-dubbo-demo

## Before start

Requires registration center, configuration center, and monitoring center

## Configuration
Application configuration example is as follows:
```yaml
dubbo:
  application:
    name: # application name
  registry:
    address: # registration center address
  metadata-report:
    address: # monitoring center
  config-center:
    address: # configuration center
  scan:
    base-packages: io.cloudapp.microservice.aliyun.demo

spring:
  cloud:
    nacos:
      config:
        import-check:
          enabled: false
```

## Run demo

Open the DubboConsumerDemoApplication class and run it.