package org.katan.yoki.util

import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import okhttp3.*
import okio.ByteString.Companion.encodeUtf8
import org.katan.yoki.*
import org.katan.yoki.protocol.*
import java.util.concurrent.*

internal fun OkHttpClient.Builder.configureOkHttpClient() = apply {
    socketFactory(UnixSocketFactory())
    dns(SocketDns())
    readTimeout(0, TimeUnit.MILLISECONDS)
    connectTimeout(0, TimeUnit.MILLISECONDS)
    callTimeout(0, TimeUnit.MILLISECONDS)
}

public actual fun createHttpClient(engine: DockerEngine): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                configureOkHttpClient()
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)

            // workaround for URL prepending
            // https://github.com/ktorio/ktor/issues/537#issuecomment-603272476
            url.takeFrom(
                URLBuilder().takeFrom(
                    URLBuilder(
                protocol = URLProtocol.HTTP,
                host = "/var/run/docker.sock".encodeUtf8().hex() + ".socket",
                port = 2375,
                encodedPath = "/v1.40/"
            )
                ).apply {
                encodedPath += url.encodedPath
            })
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
        install(UserAgent) { agent = "Yoki/0.0.1" }
    }
}