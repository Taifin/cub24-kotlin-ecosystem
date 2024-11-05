package cub.taifin

import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.timeout
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class Client(private val host: String = "http://localhost:8080/") {
    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }
    }

    suspend fun greeting(): String {
        val response = client.get("https://ktor.io/docs/")
        return response.bodyAsText()
    }

    suspend fun getBooks(): List<Pair<String, String>> {
        println("Getting books...")
        try {
            val response = client.get("$host/books") {
            }

            val deserialized = Json.decodeFromString<List<Pair<String, String>>>(response.bodyAsText())
            return deserialized
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
            return listOf()
        }
    }
}