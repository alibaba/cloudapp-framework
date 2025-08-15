# Scheduler design and implement

# Description

Distributed job are abstract designed based on SchedulerX2 and XXL-JOB, and are expected to meet the 
basic use case scenarios of both through unified abstraction. High-level use case scenarios are satisfied by calling 
the underlying native SDK through the delegate class implemented by the interface.

In the framework, interfaces are abstracted into two types: server and worker, that is, corresponding to management 
and control links and service links. The worker side interface is more used for business links, and it is provided 
for developers to use in business processing. You can define various job, such as scheduled job and delayed 
job, and support automatic creation and deletion of job by annotating _**@GlobalJob**_ unified multi-platform 
task definition method and _**GlobalJobHelper**_ unified task parameter management. The server side interface is 
more used for management and control links, mainly for service management rather than business processing, and 
supports management operations such as task groups and other job.


# Scenario

| **Feature**         | **Function/Case**                          | **Alibaba Cloud SchedulerX support** | **SchedulerX demo support**       | **Alibaba Cloud XXLJob support** | **XXLJob demo support**         |
|:--------------------|:-------------------------------------------|--------------------------------------|-----------------------------------|----------------------------------|---------------------------------|
| Core                | automatic configuration                    | <input type="checkbox" checked>      | <input type="checkbox" checked>   | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|                     | dynamic configuration                      | <input type="checkbox" checked>      | -                                 | <input type="checkbox" checked>  | -                               |
| Job execution mode  | standalone mode                            | <input type="checkbox" checked>      | <input type="checkbox" checked>   | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|                     | broadcast mode                             | <input type="checkbox" checked>      | <input type="checkbox" checked>   | -                                | -                               |
|                     | Sharding mode                              | <input type="checkbox" checked>      | <input type="checkbox" checked>   | -                                | -                               |
|                     | map mode                                   | -                                    | -                                 | -                                | -                               |
|                     | MapReduce mode                             | -                                    | -                                 | -                                | -                               |
| Job time type       | cron                                       | <input type="checkbox" checked>      | <input type="checkbox" checked>   | <input type="checkbox" checked>  | <input type="checkbox" checked> |
|                     | fixed rate                                 | <input type="checkbox" checked>      | <input type="checkbox" checked>   | <input type="checkbox" checked>  | -                               |
|                     | fixed delay                                | <input type="checkbox" checked>      | <input type="checkbox" checked>   | -                                | -                               |
|                     | calendar                                   | -                                    | -                                 | -                                | -                               |
|                     | timezone                                   | -                                    | -                                 | -                                | -                               |
| JobGroup Manager    | create/update/delete job group             | <input type="checkbox" checked>      | -                                 | -                                | -                               |
| Job Manager         | create/update/delete job                   | <input type="checkbox" checked>      | -                                 | -                                | -                               |
|                     | run job                                    | <input type="checkbox" checked>      | -                                 | -                                | -                               |
|                     | enable/disable job                         | <input type="checkbox" checked>      | -                                 | -                                | -                               |
| Job Execute Service | rerun the job instance                     | <input type="checkbox" checked>      | -                                 | -                                | -                               |
|                     | the tag job instance runs successfully     | <input type="checkbox" checked>      | -                                 | -                                | -                               |
|                     | terminate the job instance                 | <input type="checkbox" checked>      | -                                 | -                                | -                               |


# Dependencies

| **Components**                                 | **SDK**             | **Version**  |
|:-----------------------------------------------|---------------------|--------------|
| Alibaba Cloud schedulerx server implementation | schedulerx220190430 | 2.0.0        |
| Alibaba Cloud schedulerx worker implementation | schedulerx2-worker  | 1.11.5       |
| xxljob implementation                          | xxl-job-core        | 2.4.1        |


# Abstract

See the `scheduler` package in the `cloudapp-base-api` module .


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
