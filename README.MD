# Rbac Services

[![Build Status](![](https://github.com/vsfexperts/rbac/workflows/CI/badge.svg))](https://github.com/vsfexperts/rbac/actions)
[![codecov](https://codecov.io/gh/vsfexperts/rbac/branch/develop/graph/badge.svg)](https://codecov.io/gh/vsfexperts/rbac)
[![DepShield Badge](https://depshield.sonatype.org/badges/vsfexperts/rbac/depshield.svg)](https://depshield.github.io)

## Table of Contents

- [Rbac Services](#rbac-services)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Usage](#usage)
    - [Build](#build)
    - [Configuration](#configuration)
    - [Maven Plugin](#maven-plugin)
      - [Generate goal](#generate-goal)
        - [Configuration options generate goal](#configuration-options-generate-goal)
      - [Graph goal](#graph-goal)
        - [Configuration options graph goal](#configuration-options-graph-goal)
      - [Sample maven configuration](#sample-maven-configuration)
    - [Spring Boot integration](#spring-boot-integration)
      - [UserDetailsService](#userdetailsservice)
      - [OAuth2](#oauth2)
      - [Application.yaml](#applicationyaml)
    - [RBAC Sample](#rbac-sample)
  - [Modules](#modules)
  - [Limitations](#limitations)

## Overview

[RBAC](https://en.wikipedia.org/wiki/Role-based_access_control) is a best practice in order to implement a permission schema. Roles are basically mapped to a set of privileges, which are used to determine access to certain parts of the application. The default implementation of spring security doesn't include this indirection. Roles are identical to privileges. This is making it hard to modify the existing structure later on. In most cases roles do change rarely, but the privileges do change a lot due to e.g. extensions/refactorings of the existing codebase. This project is supposed to help this case, as the mapping of roles to privileges is kept within the application. This project is providing some tools in order to implement this approach.

Larger organizations do often have dedicated teams in order to manage the auth infrastructure. This could be based on active directory groups, ldap, an oauth2 provider or something else. Normally, you'd just maintain your roles in those systems and use this project in order to map the roles to privileges, which are specific to your application. This is keeping things separate. The auth team is able to maintain access on a coarse grained level (roles, e.g. admin, user) and your application is able to decide what that means exactly (in terms of access to functionality/endpoints). You'll just have to add new privileges/map roles to them in the config file (see [Configuration](#configuration)), if you're extending the functionality of your app (e.g. a new report/endpoint is added. Who's allowed to see/use it?).

## Usage

### Build

The project has to be built with Java 11. The binaries are compatible with JDK 8 and above. Maven 3.5.3 was used in development, although more current versions should be fine too. The project is not deployed to maven central, but it's possible to use the github repository by adding the following snippet to your pom.xml. Please keep in mind that you'll also have to provide github credentials in order to download those files. See [Installing a package](https://help.github.com/en/github/managing-packages-with-github-packages/installing-a-package) for details.

```xml
<repositories>
  <repository>
    <id>rbac</id>
    <name>GitHub vsfexperts Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/vsfexperts/rbac</url>
  </repository>
</repositories>
```

### Configuration

This project is based on a static mapping file, which is used to map roles to privileges

``` yaml
role: user
privileges:
  - list_orders
---
role: admin
privileges:
  - list_orders
  - update_orders
  - delete_orders
---
role: none
```

### Maven Plugin

A maven plugin is provided, which will generate code or graphs off the config file.
![Screenshot Eclipse](documentation/maven.png "Screenshot Eclipse")

#### Generate goal

This goal will generate classes with String constants. Those can be used with e.g. the `@Secured` annotation of Spring.

``` java
package de.vsfexperts.rbac.sample;

import java.lang.String;

public final class Roles {
  public static final String ADMIN = "ADMIN";

  public static final String NONE = "NONE";

  public static final String USER = "USER";
}

```

``` java
package de.vsfexperts.rbac.sample;

import java.lang.String;

public final class Privileges {
  public static final String DELETE_ORDERS = "ROLE_DELETE_ORDERS";

  public static final String LIST_ORDERS = "ROLE_LIST_ORDERS";

  public static final String UPDATE_ORDERS = "ROLE_UPDATE_ORDERS";
}
```

##### Configuration options generate goal

| Parameter               | Purpose                                                                    |
|-------------------------|----------------------------------------------------------------------------|
|rbacFile                 | Location of mapping file, default: ${basedir}/src/main/resources/rbac.yaml |
|outputDirectory          | target folder, default: ${project.build.directory}/generated-sources/rbac  |
|packageName              | base package of generated code                                             |
|springRoles              | Generate spring compatible privileges (prefixed with ROLE_), default:true  |
|privilegeClassName       | Name of privileges java class, default: Privileges                         |
|roleClassName            | Name of roles java class, default: Roles                                   |

#### Graph goal

There's also a goal to generate a graph in graphml format, which can be edited with e.g. [yEd](https://www.yworks.com/products/yed):

![Generated graph](documentation/role-mapping.png "Generated Graph")

##### Configuration options graph goal

| Parameter               | Purpose                                                                              |
|-------------------------|--------------------------------------------------------------------------------------|
|rbacFile                 | Location of mapping file, default: ${basedir}/src/main/resources/rbac.yaml           |
|outputDirectory          | target folder, default: ${project.build.directory}/generated-sources/rbac            |
|reportFileName           | location of graph, relative to output folder, default: META-INF/role-mapping.graphml |

#### Sample maven configuration

``` xml
<plugin>
  <groupId>de.vsfexperts.rbac</groupId>
  <artifactId>rbac-maven-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>generate</goal>
        <goal>graph</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <packageName>de.vsfexperts.rbac.sample</packageName>
  </configuration>
</plugin>
```

### Spring Boot integration

The spring module is basically registering some auto configurations, which will provide support for OAuth2 and the `UserDetailsService` implementation of spring, if those are available. The module can also be used on its own/in standalone mode, if you want to integrate with some different authentication provider. There's basically the `RoleMapper` class and the `RbacMappingSupplier`, which will take care of loading the mapping file and providing a service in order to convert roles to privileges. Those are loaded by `RbacMappingAutoConfiguration`. All auto configuration classes are contained in package `de.vsfexperts.rbac.spring`.

#### UserDetailsService

A custom `UserDetailsService` (`RbacUserDetailsService`) is registered in order to map roles to privileges dynamically (`RbacUserDetailsServiceAutoConfiguration`). The existing `UserDetailsService` is used as a source of data and to supply a set of roles (instead of privileges). The one from this project is decorating/wrapping the existing one in order to provide the mapping to privileges dynamically. This is simplifying a migration, as the existing authorities (e.g. active directory groups) are just treated as roles. The provided `RbacUserDetailsService` doesn't touch the other attributes of the `UserDetails` object. It's only replacing the authorities with the ones configured.

#### OAuth2

There's also support for OAuth2 and others via providing a `GrantedAuthoritiesMapper` (`RbacAuthoritiesMapper`). You'll just have to configure it in your `WebSecurityConfigurerAdapter`. See [StackOverflow Post](https://stackoverflow.com/questions/55609083/how-to-set-user-authorities-from-user-claims-return-by-an-oauth-server-in-spring) for details.

```java

@Configuration
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RbacAuthoritiesMapper authoritiesMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login().userInfoEndpoint().userAuthoritiesMapper(authoritiesMapper);
        super.configure(http);
    }

```

#### Application.yaml

Here's the full set of configurable parameters and their default values in application.yaml:

``` yaml
rbac:
  configLocation: rbac.yaml # classpath location of configuration file
  springRoles: true # generate spring compatible privileges/constants (prefixed with ROLE_)
```

### RBAC Sample

The sample is tying it all together, so it's probably a good starting point. Just launch the jar and browse to localhost:8080. The starting page is listing the users and their passwords.

## Modules

The following modules do exist:

| Module                  | Purpose                                       |
|-------------------------|-----------------------------------------------|
|rbac-configuration       | Mapping of configuration file to java objects |
|rbac-distribution        | Release of project binaries          |
|rbac-maven-plugin        | Maven code/graph generator plugin             |
|rbac-maven-plugin-it     | Integration tests of maven plugin             |
|rbac-sample              | Spring boot sample app                        |
|rbac-services            | Parent pom                                    |
|rbac-spring-boot-starter | Integration with Spring Boot                  |

## Limitations

- Graph maven plugin will generate an invalid graph, if you're using roles & privileges with same name (e.g. role: user privileges: - user). The graph will only contain 1 node, which will point to itsself.
