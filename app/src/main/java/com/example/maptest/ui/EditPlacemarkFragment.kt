package com.example.maptest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.maptest.dto.Placemark
import com.example.maptest.R
import com.example.maptest.databinding.FragmentEditPlacemarkBinding
import com.example.maptest.viewmodel.PlacemarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@AndroidEntryPoint
class EditPlacemarkFragment : BottomSheetDialogFragment() {

    private val viewModel: PlacemarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPlacemarkBinding.inflate(inflater, container, false)
        val placemark = arguments?.getParcelable<Placemark>("placemark")
        binding.placeName.setText(placemark?.name)
        binding.placeDescription.setText(placemark?.description)

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.saveBtn.setOnClickListener {
            val name = binding.placeName.text.toString()
            val description = binding.placeDescription.text.toString()
            if (binding.placeName.text.isNullOrBlank() || binding.placeDescription.text.isNullOrBlank()) {
                Toast.makeText(activity, this.getString(R.string.please_fill), Toast.LENGTH_LONG)
                    .show()
            } else {
                if (placemark != null) {
                    viewModel.edit(placemark)
                }
                viewModel.changePlacemark(placemark!!.coordinates, name, description)
                viewModel.save()
                dismiss()
            }
        }
        return binding.root
    }
}
