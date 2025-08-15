# Object storage design and implement

# Description

Distributed object storage stores data of any type and size in the form of objects, primarily used for storing large-scale unstructured data. The Framework defines a unified abstract interface for distributed object storage operations at the abstraction layer and provides implementations for Alibaba Cloud Object Storage Service (OSS) and MinIO at the implementation layer.

The abstraction layer divides the main interfaces into two categories. The first category, interfaces ending with "Service," are primarily used in business processes and are provided to developers for use in business processes. They support common operations such as uploading files, multipart uploads, and deleting objects. The second category, interfaces ending with "Manager," are primarily used in management processes and are used for object storage service management rather than business processes. They support operations such as managing and configuring buckets.

Common operations within the distributed object storage service are defined in the Framework through corresponding interface methods, which can be called directly. For advanced or unique use cases within each storage service, proxy interfaces are provided, which can be called by obtaining a storage service client.


# Scenario

| **Feature**       | **Function/Case**                                                                    | **OSS support**                 | **OSS demo support**             | **Minio support**               | **MinIO demo support**          |
|:------------------|:-------------------------------------------------------------------------------------|---------------------------------|----------------------------------|---------------------------------|---------------------------------|
| Core              | automatic configuration                                                              | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | dynamic configuration                                                                | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | -                               |
| Object Management | upload/download/update/delete object                                                 | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | copy/restore object                                                                  | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | multipart upload                                                                     | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
| Bucket Management | create/delete/get bucket                                                             | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | list object                                                                          | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | list bucket                                                                          | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | get location                                                                         | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | get object metadata                                                                  | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | -                               |
| Access Control    | manage object ACL                                                                    | <input type="checkbox" checked> | <input type="checkbox" checked>  | only query is supported         | -                               |
|                   | manage bucket ACL                                                                    | -                               | -                                | -                               | -                               |
| Data Security     | manage version                                                                       | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | upload/download based on version                                                     | -                               | -                                | -                               | -                               |
|                   | Client/Server encryption                                                             | -                               | -                                | -                               | -                               |
| Data Management   | Converted storage type lifecycle rules based on last access time                     | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | Expired life cycle rules based on last access time                                   | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | The expiration lifecycle rule that was last modified earlier than the specified time | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | Expired lifecycle rules for non-current versions after a specified time              | <input type="checkbox" checked> | <input type="checkbox" checked>  | <input type="checkbox" checked> | <input type="checkbox" checked> |
|                   | Same-Region Replication                                                              | -                               | -                                | -                               | -                               |
|                   | Cross-Region Replication                                                             | -                               | -                                | -                               | -                               |
| Data Processing   | media-processing                                                                     | -                               | -                                | -                               | -                               |


# Dependencies


| **Components**     | **SDK**        | **Version** |
|:-------------------|----------------|-------------|
| OSS implementation | aliyun-sdk-oss | 3.15.1      |
| Minio              | minio          | 8.5.11      |


# Abstract

See the `filestore` package in the `cloudapp-base-api` module .


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
