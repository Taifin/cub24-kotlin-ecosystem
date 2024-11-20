@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cub.taifin.Client
import cub.taifin.data.EntityWithCreators
import kotlinx.coroutines.launch

const val MAX_DISPLAYABLE_SIZE = 1000
const val FORCED_MAX_DISPLAYABLE_SIZE = 1500

@Composable
fun LoopedScrollableList(
    client: Client,
    maxElements: Int,
    contentIcon: ImageVector,
    producer: suspend (Client, Int) -> List<EntityWithCreators>
) {
    var pageOffset by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val items by remember { mutableStateOf(ArrayDeque<EntityWithCreators>()) }
    val listState = rememberLazyListState()

    val downUpdateNeeded by derivedStateOf {
        val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        lastVisibleItem != 0 && lastVisibleItem >= listState.layoutInfo.totalItemsCount - 40
    }

    LaunchedEffect(Unit) {
        items.addAll(producer(client, pageOffset))
        pageOffset = items.size
    }

    LaunchedEffect(downUpdateNeeded) {
        val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        // data race: effect is triggered when lastVisibleIndex is exactly at totalItems - 40
        // but then downUpdateNeeded becomes false for a moment when LoadingCard appears,
        // as there is one more item; then it is true again, and the effect is not relaunched.
        // the easiest fix is to weaken the condition
        if (lastVisibleItem != 0 && lastVisibleItem >= listState.layoutInfo.totalItemsCount - 45) {
            scope.launch {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val newItems = producer(client, pageOffset)

                    pageOffset += newItems.size
                    if (pageOffset >= maxElements) {
                        pageOffset = 0
                    }

                    items.addAll(newItems)

                    val amountToDrop = items.size - MAX_DISPLAYABLE_SIZE
                    if (items.size > FORCED_MAX_DISPLAYABLE_SIZE || (amountToDrop > 0 && listState.firstVisibleItemIndex > amountToDrop)) {
                        items.drop(amountToDrop)
                    }
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.hashCode() }) { item ->
            ItemCard(contentIcon, title = item.title, description = item.creators.joinToString("\n"))
        }
        if (downUpdateNeeded) {
            item {
                LoadingCard()
            }
        }
    }
}

@Composable
fun BooksList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LoopedScrollableList(Client(host), 65536, Icons.Filled.Book) { client, offset ->
            val books = client.getBooks(offset)
            books.map { EntityWithCreators(it.title, it.author_name) }
        }
    }

@Composable
fun MoviesList(host: String = "http://localhost:8080") =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LoopedScrollableList(Client(host), 65536, Icons.Filled.Movie) { client, offset ->
            val books = client.getMovies(page = offset / 40)
            books.map { EntityWithCreators(it.title, listOf(it.overview)) }
        }
    }

@Composable
fun ItemCard(
    icon: ImageVector,
    title: String,
    description: String,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
            ) {
                Icon(icon, "", modifier = Modifier.size(64.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun LoadingCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.HourglassTop, "")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Loading...",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}