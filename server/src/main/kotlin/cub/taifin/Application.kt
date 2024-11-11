package cub.taifin

import cub.taifin.controller.BooksController
import cub.taifin.controller.MoviesController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun Parameters.intParameterOrDefault(paramName: String, default: Int): Int = try {
    this[paramName]?.toInt() ?: default
} catch (e: NumberFormatException) {
    default
}

fun Application.configureRouting() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    val booksController = BooksController()
    val moviesController = MoviesController(System.getenv("MOVIES_API_TOKEN"))

    routing {
        staticResources("/content", "mycontent")

        get("/") {
            call.respondText("Hello World!")
        }
        get("/books") {
            var startIndex = call.request.queryParameters.intParameterOrDefault("startIndex", 0)
            if (startIndex < 0 || startIndex > 65536) startIndex = 0

            var pageSize = call.request.queryParameters.intParameterOrDefault("pageSize", 100)
            if (pageSize < 1 || pageSize > 100) pageSize = 100

            val json = Json.encodeToString(booksController.getBooks(offset = startIndex, limit = pageSize))
            call.respondText(json, ContentType.Application.Json)
        }
        get("/movies") {
            var page = call.request.queryParameters.intParameterOrDefault("page", 1)
            if (page < 1 || page > 500) page = 1

            val json = Json.encodeToString(moviesController.getMovies(page))
            call.respondText(json, ContentType.Application.Json)
        }
        get("/error-test") {
            throw IllegalStateException("Too Busy")
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}