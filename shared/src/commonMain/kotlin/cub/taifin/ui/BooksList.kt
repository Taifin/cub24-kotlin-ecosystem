package cub.taifin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cub.taifin.Client
import io.ktor.websocket.Frame.Text
import kotlinx.coroutines.launch


@Composable
fun BooksList(host: String = "http://localhost:8080/") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("Loading") }
        LaunchedEffect(true) {
            scope.launch {
                text = try {
                    println("want to get books from $host")
                    val books = Client(host).getBooks()
                    println("Got ${books.size} books")
                    books.random().first + " " + books.random().second
                } catch (e: Exception) {
                    e.localizedMessage ?: "error"
                }
            }
        }
        GreetingView(text)
    }

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}