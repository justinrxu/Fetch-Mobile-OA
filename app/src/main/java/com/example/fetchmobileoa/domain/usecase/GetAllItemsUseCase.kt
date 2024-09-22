package com.example.fetchmobileoa.domain.usecase

import com.example.fetchmobileoa.domain.model.Item
import com.example.fetchmobileoa.domain.repository.interfaces.IItemRepository
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(private val itemRepository: IItemRepository) {
    suspend operator fun invoke(): List<Item> {
        return itemRepository.getAllItems()
    }
}