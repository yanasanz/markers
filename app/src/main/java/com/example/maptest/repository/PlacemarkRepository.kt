package com.example.maptest.repository

import com.example.maptest.dto.Placemark
import kotlinx.coroutines.flow.Flow

interface PlacemarkRepository {
    val data: Flow<List<Placemark>>
    suspend fun getAll()
    suspend fun save(placemark: Placemark)
    suspend fun removeById(id: Long)
}
