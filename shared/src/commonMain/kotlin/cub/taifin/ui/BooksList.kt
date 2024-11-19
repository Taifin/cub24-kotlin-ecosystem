@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cub.taifin.Client
import kotlinx.coroutines.launch

@Composable
fun LoopedScrollableList(client: Client, maxElements: Int, producer: suspend (Client, Int) -> List<String>) {
    var pageOffset by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var items by remember { mutableStateOf(listOf<String>()) }
    val listState = rememberLazyListState()
    val updateNeeded by remember {
        derivedStateOf {
            val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
            lastVisibleItem != 0 && lastVisibleItem >= listState.layoutInfo.totalItemsCount - 40
        }
    }

    LaunchedEffect(Unit) {
        items = producer(client, pageOffset)
        pageOffset = items.size
    }

    LaunchedEffect(updateNeeded) {
        if (updateNeeded) {
            scope.launch {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val newItems = producer(client, pageOffset)
                    pageOffset += newItems.size
                    if (pageOffset >= maxElements) {
                        pageOffset = 0
                    }

                    items = items.drop(listState.firstVisibleItemIndex) + newItems
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.hashCode() }) { item ->
            ListItemView(item = item)
        }
    }
}

@Composable
fun BooksList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LoopedScrollableList(Client(host), 65536) { client, offset ->
            val books = client.getBooks(offset)
            books.mapIndexed { index, it -> "${index}. ${it.title} by ${it.author_name.joinToString(", ")}" }
        }
    }

@Composable
fun MoviesList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LoopedScrollableList(Client(host), 65536) { client, offset ->
            val books = client.getMovies(page = offset / 40)
            books.map { "${it.title} by ${it.overview}" }
        }
    }

@Composable
fun ListItemView(item: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = item)
    }
}