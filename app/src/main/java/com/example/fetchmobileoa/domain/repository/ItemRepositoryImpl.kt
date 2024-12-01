package com.example.fetchmobileoa.domain.repository

import com.example.fetchmobileoa.data.api.FetchItemsService
import com.example.fetchmobileoa.domain.exception.ApiException
import com.example.fetchmobileoa.domain.model.Item
import com.example.fetchmobileoa.domain.repository.interfaces.IItemRepository
import retrofit2.HttpException
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val fetchItemsService: FetchItemsService
): IItemRepository {
    override suspend fun getAllItems() = try {
        fetchItemsService.getFetchItems().mapNotNull { fetchItem ->
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
    } catch (e: HttpException) {
        throw ApiException("Error loading items, please try again.")
    }
}