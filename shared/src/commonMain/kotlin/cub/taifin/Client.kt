package cub.taifin

import cub.taifin.data.VolumeDto
import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class Client(private val host: String = "http://localhost:8080") {
    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 100000
        }
    }

    suspend fun getBooks(startIndex: Int = 0, maxResults: Int = 40): List<VolumeDto> {
        println("Getting books...")
        try {
            val response = client.get("$host/books") {
                parameter("startIndex", startIndex)
                parameter("pageSize", maxResults)
            }

            val deserialized = Json.decodeFromString<List<VolumeDto>>(response.bodyAsText())
            return deserialized
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
            return listOf()
        }
    }
}