@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cub.taifin.Client
import cub.taifin.data.EntityWithCreators

@Composable
fun MoviesList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LoopedScrollableList(Client(host), 65536, Icons.Filled.Movie) { client, offset ->
            val books = client.getMovies(page = offset / 40)
            books.map { EntityWithCreators(it.title, listOf(it.overview)) }
        }
    }
