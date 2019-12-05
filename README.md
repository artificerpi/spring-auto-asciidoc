# [spring-auto-asciidoc](https://plugins.gradle.org/plugin/io.github.artificerpi.spring-auto-asciidoc)

[中文](./README-zh.md)

[![Build Status](https://cloud.drone.io/api/badges/artificerpi/spring-auto-asciidoc/status.svg)](https://cloud.drone.io/artificerpi/spring-auto-asciidoc)


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

```bash
# create a new tag on master branch for release with gradle command
./gradlew clean build final
```
