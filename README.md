# spring-multi-tenancy [![Build Status](https://travis-ci.org/mdeinum/spring-multi-tenancy.svg?branch=master)](https://travis-ci.org/mdeinum/spring-multi-tenancy) [![Gitter](https://badges.gitter.im/mdeinum/spring-multi-tenancy.svg)](https://gitter.im/mdeinum/spring-multi-tenancy?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Utilizing the `TargetSource` and AOP infrastructure of Spring this enables for dynamic switching of basically anything. This has been used in production for switching `SessionFactory`'s and/or `DataSource`s based on some request parameter.
As of [Hibernate](http://www.hibernate.org) 4.1 multi-tenant support is build into hibernate, this multi-tenant support also has been integrated into this. Finally we added also support for [Spring Integration](http://projects.spring.io/spring-integration/), there is a `ChannelInterceptor` which can be added to your channels to set/get the context from a message header.

## Basic Usage

## AOP


## Hibernate

## Web

### Server
### Client

### Theme

### Spring Integration

### Spring Web Services

