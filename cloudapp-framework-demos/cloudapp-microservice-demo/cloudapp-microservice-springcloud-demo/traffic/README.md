# cloudapp-microservice-springcloud-demo

## Before start

Requires registration center

## Configuration

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: #registration center address
      config:
        import-check:
          enabled: false
```

## Run demo

Open the DemoApplication class and run it.