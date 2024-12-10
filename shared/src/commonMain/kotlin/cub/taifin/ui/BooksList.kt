@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cub.taifin.Client
import cub.taifin.data.EntityWithCreators

const val MAX_DISPLAYABLE_SIZE = 1000
const val FORCED_MAX_DISPLAYABLE_SIZE = 1500


@Composable
fun BooksList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LoopedScrollableList(Client(host), 65536, Icons.Filled.Book) { client, offset ->
            val books = client.getBooks(offset)
            books.map { EntityWithCreators(it.title, it.author_name) }
        }
    }

