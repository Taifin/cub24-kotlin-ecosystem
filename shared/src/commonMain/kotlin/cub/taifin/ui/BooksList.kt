@file:Suppress("FunctionName")

package cub.taifin.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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

    // to avoid double requests when scrolling up and down around trigger point
    val alreadyRequested by remember { mutableStateOf(mutableSetOf<Int>()) }

    val scope = rememberCoroutineScope()
    val items by remember { mutableStateOf(ArrayDeque<EntityWithCreators>()) }
    val listState = rememberLazyListState()

    val downUpdateNeeded by derivedStateOf {
        val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        lastVisibleItem >= listState.layoutInfo.totalItemsCount - 30
    }

    LaunchedEffect(downUpdateNeeded) {
        val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        // data race: effect is triggered when lastVisibleIndex is exactly at totalItems - 40
        // but then downUpdateNeeded becomes false for a moment when LoadingCard appears,
        // as there is one more item; then it is true again, and the effect is not relaunched.
        // the easiest fix is to weaken the condition
        if (pageOffset !in alreadyRequested && lastVisibleItem >= listState.layoutInfo.totalItemsCount - 35) {
            scope.launch {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    alreadyRequested.add(pageOffset)
                    val newItems = producer(client, pageOffset)

                    pageOffset += newItems.size
                    if (pageOffset >= maxElements) {
                        pageOffset = 0
                        alreadyRequested.clear()
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
        color = MaterialTheme.colorScheme.background
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
        color = MaterialTheme.colorScheme.background
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
        color = MaterialTheme.colorScheme.surface
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
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
                CircularProgressIndicator()
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

enum class DisplayMode {
    BOOKS,
    MOVIES
}

@Composable
fun MovieAndBookApp() {
    var selectedMode by remember { mutableStateOf(DisplayMode.MOVIES) }

    Column {
        Box(modifier = Modifier.weight(0.9F)) {
            when (selectedMode) {
                DisplayMode.BOOKS -> BooksList()
                DisplayMode.MOVIES -> MoviesList()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(0.1F),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedMode = DisplayMode.MOVIES },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Movie Icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Movies")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { selectedMode = DisplayMode.BOOKS },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Book Icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Books")
            }
        }
    }
}