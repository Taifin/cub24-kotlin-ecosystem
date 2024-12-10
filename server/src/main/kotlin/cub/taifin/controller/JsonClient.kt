package cub.taifin.controller

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface JsonClient {
    val apiBasePath: String
    val apiEndpoint: String
    val client: HttpClient

    suspend fun doApiRequest(vararg parameters: Pair<String, Any>, authToken: String? = null): HttpResponse {
        val request = client.request {
            method = HttpMethod.Get
            url {
                takeFrom(apiBasePath)
                appendPathSegments(apiEndpoint)
            }

            contentType(ContentType.Application.Json)

            if (authToken != null) header("Authorization", "Bearer $authToken")

            parameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }

        return request
    }
}