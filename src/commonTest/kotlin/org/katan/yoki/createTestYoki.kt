package org.katan.yoki

/**
 * Creates a new Yoki instance for testing.
 * @param block The client configuration factory.
 */
fun createTestYoki(block: YokiConfigBuilder.() -> Unit = {}): Yoki {
    return runCatching {
        Yoki { apply(block) }
    }.onFailure {
        throw RuntimeException("Failed to initialize Yoki test client", it)
    }.getOrThrow()
}
