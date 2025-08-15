# Messaging design and implement

# Description

In the Framework distributed message abstraction layer, the three main interfaces used by the communication model are abstracted, namely: the consumer interface Conusmer, the producer interface Producer, and the listener interface Notifier.

In high-level consumer/producer use case scenarios that require the use of various MQ systems, such as custom consumption logic and consumption retries, the interface defines the method of obtaining the underlying delegated consumer/producer object, which can be processed by calling the corresponding MQ system's native SDK.

In scenarios where you work in a way that uses spring stream, you can turn off the Framework's message driver by configuring the Framework. When turned off, the Framework's messaging mechanism will follow the native message-driven implementation exactly.

# Scenario

> MQ framework native message: For example, the 'ConsumerRecord' class in the 'kafka-clients' framework
> 
> MQ framework native consumer:  For example, the 'KafkaConsumer' class in the 'kafka-clients' framework

| **Feature**      | **Function/Case**                                | **Alibaba Cloud Kafka support** | **Kafka demo support**           | **Alibaba Cloud RocketMQ support**  | **RocketMQ demo support**       |
|:-----------------|:-------------------------------------------------|---------------------------------|----------------------------------|-------------------------------------|---------------------------------|
| Core             | automatic configuration                          | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked>     | <input type="checkbox" checked> |
|                  | dynamic configuration                            | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked>     | <input type="checkbox" checked> |
| Message Producer | send/asynSend simple message                     | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked>     | <input type="checkbox" checked> |
|                  | send/asynSend MQ framework native message        | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked>     | -                               |
|                  | send/asynSend MQMessage                          | <input type="checkbox" checked> | -                                | <input type="checkbox" checked>     | -                               |
| Message Consumer | consume MQ framework native message              | -                               | -                                | <input type="checkbox" checked>     | -                               |
|                  | consume MQMessage                                | -                               | -                                | <input type="checkbox" checked>     | <input type="checkbox" checked> |
|                  | MQ framework native consumer consumption message | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked>     | -                               |
| Data encryption  | SASL\SSL                                         | <input type="checkbox" checked> | <input type="checkbox" checked>  | -                                   | -                               |
| Message Type     | simple message                                   | -                               | -                                | <input type="checkbox" checked>     | <input type="checkbox" checked> |
|                  | ordered message                                  | -                               | -                                | -                                   | -                               |
|                  | transaction message                              | -                               | -                                | -                                   | -                               |
|                  | scheduled message                                | -                               | -                                | -                                   | -                               |
|                  | fixed delay time message                         | -                               | -                                | -                                   | -                               |
| Message Function | message filter                                   | <input type="checkbox" checked> | -                                | <input type="checkbox" checked>     | -                               |
|                  | message retry                                    | -                               | -                                | -                                   | -                               |
|                  | clustering consumption\broadcasting-consumption  | -                               | -                                | <input type="checkbox" checked>     | -                               |

# Dependencies

| **Components**           | **SDK**                       | **Version**  |
|:-------------------------|-------------------------------|--------------|
| kafka implementation     | spring-kafka                  | 2.8.11       |
|                          | kafka-clients                 | 3.1.2        |
| rocketmq implementation  | alibabacloud-rocketmq20220801 | 1.0.12       |
|                          | rocketmq-spring-boot          | 2.3.0        |


# Abstract

See the `messaging` package in the `cloudapp-base-api` module .


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
