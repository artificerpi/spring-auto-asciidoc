# [spring-auto-asciidoc](https://plugins.gradle.org/plugin/io.github.artificerpi.spring-auto-asciidoc)

[中文](./README-zh.md)

[![Build Status](https://cloud.drone.io/api/badges/artificerpi/spring-auto-asciidoc/status.svg)](https://cloud.drone.io/artificerpi/spring-auto-asciidoc)

  [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=artificerpi_spring-auto-asciidoc&metric=alert_status)](https://sonarcloud.io/dashboard?id=artificerpi_spring-auto-asciidoc) 
 [![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=artificerpi_spring-auto-asciidoc&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=artificerpi_spring-auto-asciidoc)
 [![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=artificerpi_spring-auto-asciidoc&metric=bugs)](https://sonarcloud.io/component_measures/metric/reliability_rating/list?id=artificerpi_spring-auto-asciidoc)
 [![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=artificerpi_spring-auto-asciidoc&metric=vulnerabilities)](https://sonarcloud.io/component_measures/metric/security_rating/list?id=artificerpi_spring-auto-asciidoc)
 
## Usage

```groovy
plugins {
  // More plugins ...
  id "io.github.artificerpi.spring-auto-asciidoc" version "0.3"
}

// Below your deps configuration ...

asciidoctor {
    attributes "classname-pattern": "(\\w+)-rest-controller-tests"
}
```

No extra configuration is needed!

## Build and release
Release of this plugin is managed with `nebula.release` plugin.

For covenience, just create a new git tag in `<major>.<minor>.<patch>` format, e.g, `0.0.1`.

> Tips: Merge codes and tags ref from master branch into develop again before new release lifecycle.

```bash
# create a new tag on master branch for release with gradle command
./gradlew clean build final
```
