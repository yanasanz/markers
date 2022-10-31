package com.example.maptest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.maptest.*
import com.example.maptest.databinding.FragmentPlacesListBinding
import com.example.maptest.dto.Placemark
import com.example.maptest.viewmodel.PlacemarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesListFragment : Fragment() {

    lateinit var binding:FragmentPlacesListBinding

    private val viewModel: PlacemarkViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPlacesListBinding.inflate(inflater, container, false)

        val adapter = PlacemarkAdapter(object : OnInteractionListener {
            override fun onEdit(placemark: Placemark) {
                viewModel.edit(placemark)
                val bundle = Bundle()
                bundle.putParcelable("placemark", placemark)
                val editPlacemarkFragment = EditPlacemarkFragment()
                editPlacemarkFragment.show(parentFragmentManager, "TAG")
                editPlacemarkFragment.arguments = bundle
            }

            override fun onRemove(placemark: Placemark) {
                viewModel.removeById(placemark.id)
            }

            override fun onShowLocation(placemark: Placemark) {
                val bundle = Bundle()
                bundle.putParcelable("placemark", placemark)
                findNavController().navigate(R.id.action_placesListFragment_to_mapFragment, bundle)
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { markers ->
            adapter.submitList(markers)
        }

        return binding.root
    }
}