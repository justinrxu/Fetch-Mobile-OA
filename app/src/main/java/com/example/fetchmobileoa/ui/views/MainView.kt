package com.example.fetchmobileoa.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fetchmobileoa.domain.model.Item
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val uiState: MainViewModel.MainUIState by
        mainViewModel.mainUIState.collectAsStateWithLifecycle()

    val cardWidth: Dp = 120.dp
    val cardSpacing = 6.dp
    val totalCardWidth = cardWidth + (2 * cardSpacing)
    val numColumns = (screenWidth / totalCardWidth).toInt().coerceAtLeast(1)

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        // LazyColumn over LazyHorizontalGrid for stickyHeader
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(cardSpacing),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 30.dp, vertical = 15.dp)
        ) {
            item {
                Text(text = "Fetch Items", style = MaterialTheme.typography.titleLarge)
            }

            uiState.let { state ->
                when (state) {
                    is MainViewModel.MainUIState.Loaded -> {
                        val itemsGroupedByListId = state.items
                            .groupBy { item ->
                                item.listId
                            }.map { (listId, items) ->
                                listId to items.sortedBy { item ->
                                    item.name
                                }
                            }
                        itemsGroupedByListId.forEachIndexed { index, (listId, items) ->
                            val toggled = state.expanded[listId] ?: false

                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "ListID: $listId",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                state.expanded[listId] = !toggled

                                                // Scroll to maintain same sticky header
                                                var i = 0
                                                var listIdPosition = 0
                                                while (i < index) {
                                                    listIdPosition++
                                                    val (curListId, curListIdItems) =
                                                        itemsGroupedByListId[i]
                                                    if (state.expanded[curListId] == true) {
                                                        listIdPosition +=
                                                            (curListIdItems.size + 2) / 3
                                                    }

                                                    i++
                                                }

                                                if (
                                                    state.expanded[listId] == false &&
                                                    lazyListState.firstVisibleItemIndex >
                                                    listIdPosition
                                                ) {
                                                    scope.launch {
                                                        lazyListState.animateScrollToItem(
                                                            listIdPosition + 1
                                                        )
                                                    }
                                                }
                                            }
                                            .padding(4.dp)
                                    )
                                }
                            }

                            if (toggled) {
                                items.chunked(numColumns).forEach { rowItems ->
                                    item {
                                        Row(
                                            horizontalArrangement =
                                                Arrangement.spacedBy(cardSpacing),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .animateItem(),
                                        ) {
                                            rowItems.forEach { item ->
                                                Item(item = item, cardWidth = cardWidth)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    MainViewModel.MainUIState.Loading -> {
                        item {
                            CircularProgressIndicator()
                        }
                    }

                    is MainViewModel.MainUIState.Error -> {
                        item {
                            state.e?.let { Text(text = state.e) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Item(
    item: Item,
    cardWidth: Dp = 120.dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(cardWidth)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "ID: ${item.id}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(text = item.name, overflow = TextOverflow.Ellipsis)
    }
}
