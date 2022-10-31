package com.example.maptest.db

import android.content.Context
import androidx.room.Room
import com.example.maptest.dao.PlacemarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): AppDb = Room.databaseBuilder(
        context,
        AppDb::class.java,
        "App.db"
    ).build()

    @Provides
    fun providePlaceDao(appDB: AppDb): PlacemarkDao = appDB.placeDao()
}