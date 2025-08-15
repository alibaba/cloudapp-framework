# Observabilities design and implement

# Description

Observability is designed based on [OpenTelemetry](https://opentelemetry.io/) (hereinafter referred to as OTel) to 
provide observability of metrics, logs, and traces. For metrics, the abstraction defines annotations related to the 
metrics: Metric Collector `@MetricCollection`, Metric `@Metric`, Method Counter `@InvokeCount`, which provide metric 
statistical annotations for field granularity and method granularity. For logs, the Log4J2 framework logs are 
associated with trace data such as traceid and spanid by automatically modifying the log configuration. For tracking,
the ability to identify traffic is provided through the tracking service.


# Scenario

| **Feature**          | **Function/Case**                                         | **OpenTelemetry support**       | **demo support**                  |
|:---------------------|:----------------------------------------------------------|---------------------------------|-----------------------------------|
| Core                 | automatic configuration                                   | <input type="checkbox" checked> | <input type="checkbox" checked>   |
|                      | dynamic configuration                                     | <input type="checkbox" checked> | <input type="checkbox" checked>   |
| Observabilities      | generate  metrics                                         | <input type="checkbox" checked> | <input type="checkbox" checked>   |
|                      | prometheus exporter                                       | <input type="checkbox" checked> | <input type="checkbox" checked>   |
|                      | otel metrics endpoint                                     | <input type="checkbox" checked> | <input type="checkbox" checked>   |
|                      | the log configuration is like traceid                     | <input type="checkbox" checked> | <input type="checkbox" checked>   |
|                      | Propagation                                               | <input type="checkbox" checked> | <input type="checkbox" checked>   |


# Dependencies

| **Components**               | **SDK**                                             | **Version**  |
|:-----------------------------|-----------------------------------------------------|--------------|
| OpenTelemetry implementation | opentelemetry-instrumentation-annotations           | 2.6.0        |
|                              | opentelemetry-api                                   | 1.43.0       |
|                              | opentelemetry-sdk                                   | 1.43.0       |
|                              | opentelemetry-semconv                               | 1.28.0-alpha |
|                              | opentelemetry-context                               | 1.43.0       |
|                              | opentelemetry-log4j-context-data-2.17-autoconfigure | 2.9.0-alpha  |
|                              | opentelemetry-exporter-otlp                         | 1.43.0       |
|                              | opentelemetry-exporter-prometheus                   | 1.43.0-alpha |


# Abstract

See the `observabilities` package in the `cloudapp-base-api` module .


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
