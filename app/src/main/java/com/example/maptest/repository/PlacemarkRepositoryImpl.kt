package com.example.maptest.repository

import com.example.maptest.dao.PlacemarkDao
import com.example.maptest.dto.Placemark
import com.example.maptest.entity.PlacemarkEntity
import com.example.maptest.entity.toDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlacemarkRepositoryImpl @Inject constructor (
    private val placemarkDao: PlacemarkDao
) : PlacemarkRepository {

    override val data: Flow<List<Placemark>> = placemarkDao.getAll()
        .map(List<PlacemarkEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        placemarkDao.getAll()
    }

    override suspend fun save(placemark: Placemark) {
        if (placemark.id == 0L) {
            placemarkDao.insert(PlacemarkEntity.fromDto(placemark))
        } else {
            placemarkDao.update(PlacemarkEntity.fromDto(placemark))
        }
    }

    override suspend fun removeById(id: Long) {
        placemarkDao.removeById(id)
    }
}