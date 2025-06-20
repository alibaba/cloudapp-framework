# 设计与使用说明

# 设计目标

CloudApp框架的目标是旨在设计一套通过统一的 SDK 封装云厂商的服务之后，解耦各个云厂商对应 PaaS 服务的编程差异，达到一套代码在不同厂商间灵活交付的效果。

![alt text](docs/assets/CloudApp.jpg)

当前的应用维度的包组织结构如下，在一个应用构建的过程中，将包括通用的 starter 依赖与对应云产品功能的实现的依赖，如下所示：

![alt text](docs/assets/cloudapp-framework-uml.png)

在框架统一的 SDK 实现中，包括：分布式对象存储、分布式消息、分布式缓存、分布式配置等平台服务，也包括：全局序列、数据源等通用服务，还包括：微服务治理、可观测、应用服务器 aliee 和 AI 平台等。除了将 SDK 统一之外，框架还提供各服务的动态刷新与监控能力。此外，框架适配多版本 JDK、SpringBoot。

设计与使用说明如下：

[分布式对象存储设计与使用](docs/分布式对象存储设计与使用.md)

[分布式消息设计与使用](docs/分布式消息设计与使用.md)

[分布式缓存设计与使用](docs/分布式缓存设计与使用.md)

[分布式配置设计与使用](docs/分布式配置设计与使用.md)

[分布式搜索设计与使用](docs/分布式搜索设计与使用.md)

[服务网关设计与使用](docs/服务网关设计与使用.md)

[微服务治理设计与使用](docs/微服务治理设计与使用.md)

[可观测设计与使用](docs/可观测设计与使用.md)

[数据源设计与使用](docs/数据源设计与使用.md)

[线程池设计与使用](docs/线程池设计与使用.md)

[分布式任务设计与使用](docs/分布式任务设计与使用.md)

[OAuth2应用设计与使用](docs/OAuth2应用设计与使用.md)

[事务管理设计与使用](docs/事务管理设计与使用.md)

[邮件设计与使用](docs/邮件设计与使用.md)

[全局序列设计与使用](docs/全局序列设计与使用.md)

[工具类设计与使用](docs/工具类设计与使用.md)

[异常设计与使用](docs/异常设计与使用.md)