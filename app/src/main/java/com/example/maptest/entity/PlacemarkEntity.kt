package com.example.maptest.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.maptest.dto.Placemark
import com.yandex.mapkit.geometry.Point

@Entity
data class PlacemarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val description: String
) {
    fun toDto() = Placemark(
        id,
        Point(latitude, longitude),
        name,
        description
    )

    companion object {
        fun fromDto(dto: Placemark) = PlacemarkEntity(
            dto.id,
            dto.coordinates.latitude,
            dto.coordinates.longitude,
            dto.name,
            dto.description
        )
    }
}

fun List<PlacemarkEntity>.toDto(): List<Placemark> = map(PlacemarkEntity::toDto)
fun List<Placemark>.toEntity(): List<PlacemarkEntity> = map(PlacemarkEntity::fromDto)

