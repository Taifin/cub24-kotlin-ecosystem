package cub.taifin.controller

import cub.taifin.data.BookInfo
import cub.taifin.data.DocsDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class BooksController : JsonClient {
    override val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 100000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override val apiBasePath = "https://openlibrary.org"
    override val apiEndpoint = "/search.json"

    companion object {
        val fields = "fields" to "title,author_name"
        val query = "q" to "language:eng"
    }

    suspend fun getBooks(offset: Int = 0, limit: Int = 100): List<BookInfo> {
        val response = doApiRequest(query, fields, "offset" to offset, "limit" to limit)
        val deserializedResponse = response.body<DocsDto>()
        return deserializedResponse.docs
    }
}