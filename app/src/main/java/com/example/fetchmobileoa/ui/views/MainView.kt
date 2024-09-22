package com.example.fetchmobileoa.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(mainViewModel: MainViewModel) {
    val uiState: MainViewModel.MainUIState by
        mainViewModel.mainUIState.collectAsStateWithLifecycle()

    (uiState as? MainViewModel.MainUIState.Loaded)?.let { loadedUIState ->
        val expandedListIdMap: SnapshotStateMap<Int, Boolean> = remember {
            mutableStateMapOf()
        }

        val itemsMapByListId = loadedUIState.items.groupBy { it.listId }.toSortedMap()

        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 30.dp, vertical = 15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                item {
                    Text(text = "Fetch Items", style = MaterialTheme.typography.titleLarge)
                }
                itemsMapByListId.forEach { (listId, items) ->
                    val toggled = expandedListIdMap[listId] ?: false
                    stickyHeader {
                        Surface(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "ListID $listId",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .clickable {
                                        expandedListIdMap[listId] = !toggled
                                    }
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }
                    item {
                        AnimatedVisibility(visible = toggled) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                items.sortedBy { it.name }.forEach { item ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                                        Column {
                                            Text(
                                                text = "Item ID",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                            Text(text = "${item.id}")
                                        }
                                        Column {
                                            Text(
                                                text = "Name",
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                            Text(text = item.name)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}