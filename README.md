# [spring-auto-asciidoc](https://plugins.gradle.org/plugin/io.github.artificerpi.spring-auto-asciidoc)

[中文](./README-zh.md)

[![Build Status](https://cloud.drone.io/api/badges/artificerpi/spring-auto-asciidoc/status.svg)](https://cloud.drone.io/artificerpi/spring-auto-asciidoc)

> Due to approval delay in [Gradle pluins Site](https://plugins.gradle.org/plugin/io.github.artificerpi.spring-auto-asciidoc), this plugin is only published into github package.

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
