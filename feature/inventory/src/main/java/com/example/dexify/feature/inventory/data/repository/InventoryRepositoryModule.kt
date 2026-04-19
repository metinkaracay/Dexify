package com.example.dexify.feature.inventory.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InventoryRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        impl: OfflineFirstInventoryRepository
    ): InventoryRepository
}
