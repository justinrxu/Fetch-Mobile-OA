package com.example.fetchmobileoa.domain.di

import com.example.fetchmobileoa.domain.repository.ItemRepositoryImpl
import com.example.fetchmobileoa.domain.repository.interfaces.IItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun provideItemRepository(itemRepositoryImpl: ItemRepositoryImpl): IItemRepository
}