package dev.gaphunter.ktorhttpclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinClientBuildFinderTest : BasePlatformTestCase() {

    fun `test client built inside a regular function is flagged`() {
        val file = myFixture.configureByText(
            "ApiService.kt",
            """
            class ApiService {
                fun fetch() {
                    val client = HttpClient(CIO)
                    client.get("https://example.com")
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test client built with a config lambda inside a function is flagged`() {
        val file = myFixture.configureByText(
            "ApiService.kt",
            """
            class ApiService {
                fun fetch() {
                    val client = HttpClient(CIO) {
                        install(ContentNegotiation)
                    }
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test client built as a class property is not flagged`() {
        val file = myFixture.configureByText(
            "ApiService.kt",
            """
            class ApiService {
                val client = HttpClient(CIO)
                fun fetch() {
                    client.get("https://example.com")
                }
            }
            """.trimIndent(),
        )
        assertEquals(0, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test client built inside a constructor is not flagged`() {
        val file = myFixture.configureByText(
            "ApiService.kt",
            """
            class ApiService {
                val client: HttpClient
                constructor() {
                    client = HttpClient(CIO)
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated constructor call named HttpClient in a comment or different context is not affected`() {
        val file = myFixture.configureByText(
            "ApiService.kt",
            """
            class ApiService {
                fun describe(): String {
                    return "uses HttpClient internally"
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }
}
