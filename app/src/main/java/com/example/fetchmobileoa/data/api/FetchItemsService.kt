package com.example.fetchmobileoa.data.api

import com.example.fetchmobileoa.data.entity.FetchItem
import retrofit2.http.GET

interface FetchItemsService {
    @GET("hiring.json")
    suspend fun getFetchItems(): List<FetchItem>
}