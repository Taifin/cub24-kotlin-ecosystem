package cub.taifin.controller

import cub.taifin.data.MovieInfo
import cub.taifin.data.MoviesDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MoviesController(private val apiKey: String) : JsonClient {
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

    override val apiBasePath = "https://api.themoviedb.org/3"
    override val apiEndpoint = "/discover/movie"

    suspend fun getMovies(page: Int): List<MovieInfo> {
        val response = doApiRequest("page" to page, authToken = apiKey)
        val deserializedResponse = response.body<MoviesDto>()
        return deserializedResponse.results
    }
}