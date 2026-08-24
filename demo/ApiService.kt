// Demo data for Ktor HttpClient Reuse Companion -- used with
// `./gradlew runIde` to capture the real Marketplace screenshot. Open
// this file, the warning should appear on the HttpClient(CIO) line
// inside fetchOrdersUnsafely.

class ApiService {

    private val client: HttpClient

    constructor() {
        // Built once, in the constructor -- NOT flagged.
        client = HttpClient(CIO)
    }

    suspend fun fetchOrdersUnsafely(): String {
        // A new client built here on every call -- FLAGGED. Each
        // instance spins up its own engine and connection pool.
        val client = HttpClient(CIO)
        return client.get("https://api.example.com/orders").bodyAsText()
    }

    suspend fun fetchOrdersSafely(): String {
        // Reuses the instance built once in the constructor -- NOT
        // flagged.
        return client.get("https://api.example.com/orders").bodyAsText()
    }
}
