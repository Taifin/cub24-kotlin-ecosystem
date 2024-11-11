package cub.taifin.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object DataController {
    private val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 100000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private const val GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1"
    private const val GOOGLE_BOOKS_LIST_VOLUMES_ENDPOINT = "/volumes"

    private suspend fun doApiRequest(query: String, startIndex: Int, maxResults: Int): HttpResponse {
        val request = client.request {
            method = HttpMethod.Get
            url {
                takeFrom(GOOGLE_BOOKS_API_URL)
                appendPathSegments(GOOGLE_BOOKS_LIST_VOLUMES_ENDPOINT)
            }

            contentType(ContentType.Application.Json)

            parameter("q", query)
            parameter("startIndex", startIndex)
            parameter("maxResults", maxResults)
        }

        return request
    }

    suspend fun getBooks(query: String = "*", startIndex: String = "0", maxResults: String = "10"): List<VolumeDto> {
        val startIndexInt = try {
            startIndex.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        val maxResultsInt = try {
            maxResults.toInt()
        } catch (e: NumberFormatException) {
            20
        }

        val response = doApiRequest(query, startIndexInt, maxResultsInt)
        val deserializedResponse = response.body<ItemizedResponse<VolumeDto>>()
        return deserializedResponse.items
    }
}