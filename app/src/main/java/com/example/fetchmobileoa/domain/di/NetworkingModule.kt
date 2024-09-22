package com.example.fetchmobileoa.domain.di

import com.example.fetchmobileoa.data.api.FetchItemsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesFetchItemsService(retrofit: Retrofit): FetchItemsService {
        return retrofit.create(FetchItemsService::class.java)
    }
}