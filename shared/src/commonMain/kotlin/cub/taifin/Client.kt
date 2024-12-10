package cub.taifin

import cub.taifin.data.BookInfo
import cub.taifin.data.MovieInfo
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class Client(private val host: String = "http://localhost:8080") {
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 100000
        }
        install(Logging) {
            level = LogLevel.INFO
            logger = Logger.DEFAULT
        }
    }

    suspend fun getBooks(startIndex: Int = 0, size: Int = 40): List<BookInfo> {
        try {
            val response = client.get("$host/books") {
                parameter("startIndex", startIndex)
                parameter("limit", size)
            }

            val deserialized = Json.decodeFromString<List<BookInfo>>(response.bodyAsText())
            return deserialized
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
            return listOf()
        }
    }

    suspend fun getMovies(page: Int = 1): List<MovieInfo> {
        try {
            val response = client.get("$host/movies") {
                parameter("page", page)
            }

            val deserialized = Json.decodeFromString<List<MovieInfo>>(response.bodyAsText())
            return deserialized
        } catch (e: Exception) {
            println("Caught exception: ${e.message}")
            return listOf()
        }
    }
}