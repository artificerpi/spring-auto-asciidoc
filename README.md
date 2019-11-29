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
