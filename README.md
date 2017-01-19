# spring-multi-tenancy

[![Build Status](https://travis-ci.org/mdeinum/spring-multi-tenancy.svg?branch=master)](https://travis-ci.org/mdeinum/spring-multi-tenancy)
[![codecov](https://codecov.io/gh/mdeinum/spring-multi-tenancy/branch/master/graph/badge.svg)](https://codecov.io/gh/mdeinum/spring-multi-tenancy)
[![Join the chat at https://gitter.im/mdeinum/spring-multi-tenancy](https://badges.gitter.im/mdeinum/spring-multi-tenancy.svg)](https://gitter.im/mdeinum/spring-multi-tenancy?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2543de5c54a245029a94b8bbe2266c71)](https://www.codacy.com/app/mdeinum/spring-multi-tenancy?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mdeinum/spring-multi-tenancy&amp;utm_campaign=Badge_Grade)

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

