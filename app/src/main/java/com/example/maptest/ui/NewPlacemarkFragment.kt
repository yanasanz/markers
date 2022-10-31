package com.example.maptest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.maptest.R
import com.example.maptest.databinding.FragmentNewPlacemarkBinding
import com.example.maptest.viewmodel.PlacemarkViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPlacemarkFragment : BottomSheetDialogFragment() {

    private val viewModel: PlacemarkViewModel by viewModels()

    lateinit var binding: FragmentNewPlacemarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlacemarkBinding.inflate(inflater, container, false)
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0
        val coordinates = Point(latitude, longitude)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.saveButton.setOnClickListener {
            val name = binding.placemarkName.text.toString()
            val description = binding.placemarkDescription.text.toString()

            if (binding.placemarkName.text.isNullOrBlank() || binding.placemarkDescription.text.isNullOrBlank()) {
                Toast.makeText(activity, this.getString(R.string.please_fill), Toast.LENGTH_LONG)
                    .show()
            } else {
                viewModel.changePlacemark(coordinates, name, description)
                viewModel.save()
                dismiss()
            }
        }
        return binding.root
    }

}