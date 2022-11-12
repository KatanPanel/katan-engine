# Yoki

[![Build](https://github.com/KatanPanel/yoki/actions/workflows/build.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/build.yml)
[![Integration Tests](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/KatanPanel/yoki/actions/workflows/integration-tests.yml)

Yoki allows you to interact with the Docker Engine Remote API in a simplified and fast way.

* [Configuration](#configuration)
* [Basic Usage](#basic-usage)
* [Versions Compatibility](#versions-compatibility)
* [Native Targets](#native-targets)
* [Interoperability](#interoperability)

```gradle
dependencies {
    implementation("org.katan:yoki:0.0.1")
}
```

## Configuration

By default, at client startup if no configuration parameters are passed, the settings that will be applied will depend
on the current platform and environment variables.

For example, the socket path will be set to the value of
the [`DOCKER_HOST` environment variable](https://docs.docker.com/compose/reference/envvars/#docker_host) if set,
otherwise it will use the platform default.

```kotlin
import org.katan.yoki

val client = Yoki()
```

You can still configure the client by expanding the initialization block

```kotlin
Yoki {
    // this: YokiConfigBuilder
}
```

Change socket path (docker host) or target api version

```kotlin
Yoki {
    socketPath = "unix:///var/run/docker.sock"
    apiVersion = "1.40"
}
```

## Basic Usage

The way to access resources is straight to the point, all functions (for Kotlin) are suspend.

##### Get info about system version

```kotlin
import org.katan.yoki
import org.katan.yoki.models.system.SystemVersion
import org.katan.yoki.resource.list

val client = Yoki()

val version: SystemVersion = client.system.version()
```

##### Listing all containers

```kotlin
import org.katan.yoki
import org.katan.yoki.resource.list

val client = Yoki()

client.containers.list {
    all = true
}
```

##### Creating a new network

```kotlin
import org.katan.yoki
import org.katan.yoki.resource.create

val client = Yoki()

client.networks.create {
    name = "octopus-net"
    driver = "overlay"
}
```

##### Streaming container logs

Streaming methods will always return a [Flow](https://kotlinlang.org/docs/flow.html).

```kotlin
import org.katan.yoki
import org.katan.yoki.models.Frame
import org.katan.yoki.resource.logs

val client = Yoki()

val flow: Flow<Frame> = yoki.containers.logs("floral-fury") {
    stderr = true
    stdout = true
}
```

#### Fallback to version-specific parameter value

By default, all options parameters for accessing a resource use `null`, that is, *null value* means that it will use the
value defined by the Docker API as the default value for that property.

## Versions Compatibility

TODO

## Native Targets

For now the only supported native targets are: macosX64, linuxX64 and mingwX64.

## Interoperability

TODO

## License

Yoki is licensed under the MIT license.