package com.example.fetchmobileoa.domain.repository.interfaces

import com.example.fetchmobileoa.domain.model.Item

interface IItemRepository {
    suspend fun getAllItems(): List<Item>
}