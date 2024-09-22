package com.example.fetchmobileoa.domain.repository

import com.example.fetchmobileoa.data.api.FetchItemsService
import com.example.fetchmobileoa.domain.model.Item
import com.example.fetchmobileoa.domain.repository.interfaces.IItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val fetchItemsService: FetchItemsService
): IItemRepository {
    override suspend fun getAllItems() = fetchItemsService.getFetchItems().mapNotNull { fetchItem ->
        if (fetchItem.name?.isBlank() == false) {
            Item(
                id = fetchItem.id,
                listId = fetchItem.listId,
                name = fetchItem.name
            )
        } else {
            null
        }
    }
}