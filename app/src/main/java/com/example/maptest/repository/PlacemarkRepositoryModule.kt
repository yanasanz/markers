package com.example.maptest.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlacemarkRepositoryModule {

    @Binds
    @Singleton
    fun bindRepositoryImpl(placemarkRepository: PlacemarkRepositoryImpl): PlacemarkRepository
}