package com.example.maptest.viewmodel

import com.yandex.mapkit.geometry.Point
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.maptest.dto.Placemark
import com.example.maptest.repository.PlacemarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private val empty = Placemark(
    0,
    Point(0.0, 0.0),
    "",
    ""
)

@HiltViewModel
class PlacemarkViewModel @Inject constructor(
    private val repository: PlacemarkRepository
) : ViewModel() {

    val data = repository.data.asLiveData()
    val edited = MutableLiveData(empty)

    fun edit(place: Placemark) {
        edited.value = place
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                repository.save(it)
            }
        }
        edited.value = empty
    }

    fun changePlacemark(coords: Point, name: String, description: String) {
        val nameText = name.trim()
        val descriptionText = description.trim()

        if (edited.value?.name == nameText && edited.value?.description == descriptionText) {
            return
        }

        if (edited.value?.id == 0L) {
            edited.value = edited.value?.copy(
                coordinates = coords,
                name = nameText,
                description = descriptionText
            )
        } else {
            edited.value = edited.value?.copy(
                name = nameText,
                description = descriptionText
            )
        }
    }
}