package com.example.maptest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maptest.dao.PlacemarkDao
import com.example.maptest.entity.PlacemarkEntity

@Database(entities = [PlacemarkEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun placeDao(): PlacemarkDao
}