package cub.taifin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cub.taifin.Client
import cub.taifin.data.EntityWithCreators
import kotlinx.coroutines.launch

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
        lastVisibleItem >= listState.layoutInfo.totalItemsCount - 10
    }

    LaunchedEffect(downUpdateNeeded) {
        val lastVisibleItem = listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size
        // data race: effect is triggered when lastVisibleIndex is exactly at totalItems - 40
        // but then downUpdateNeeded becomes false for a moment when LoadingCard appears,
        // as there is one more item; then it is true again, and the effect is not relaunched.
        // the easiest fix is to weaken the condition
        if (pageOffset !in alreadyRequested && lastVisibleItem >= listState.layoutInfo.totalItemsCount - 15) {
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
