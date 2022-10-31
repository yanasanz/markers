package com.example.maptest.dto

import android.os.Parcelable
import com.yandex.mapkit.geometry.Point
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Placemark(
    val id: Long = 0L,
    val coordinates: @RawValue Point,
    val name: String = "",
    val description: String = ""
) : Parcelable