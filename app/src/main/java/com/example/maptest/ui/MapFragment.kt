package com.example.maptest.ui

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.maptest.*
import com.example.maptest.databinding.FragmentMapBinding
import com.example.maptest.dto.Placemark
import com.example.maptest.viewmodel.PlacemarkViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), InputListener, UserLocationObjectListener {

    private val TARGET_LOCATION = Point(59.945933, 30.320045)
    private val viewModel: PlacemarkViewModel by viewModels()
    private lateinit var collection: MapObjectCollection
    private lateinit var binding: FragmentMapBinding
    private lateinit var markerTapListener: MapObjectTapListener
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var locationMapkit: MapKit
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        mapView = binding.mapview
        collection = mapView.map.mapObjects.addCollection()
        locationMapkit = MapKitFactory.getInstance()
        mapView.map.addInputListener(this)

        userLocationLayer = locationMapkit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.setObjectListener(this)
        binding.myLocation.setOnClickListener {
            if (userLocationLayer.isVisible) {
                userLocationLayer.setVisible(false)
            } else {
                userLocationLayer.setVisible(true)
            }
        }

        val placemark = arguments?.getParcelable<Placemark>("placemark")

        if (placemark != null) {
            binding.showInfo.visibility = VISIBLE
            val mapObject = collection.addPlacemark(
                Point(placemark.coordinates.latitude, placemark.coordinates.longitude),
                ImageProvider.fromBitmap(
                    context?.let {
                        AppCompatResources.getDrawable(
                            it,
                            R.drawable.ic_baseline_my_location_48
                        )?.toBitmap()
                    } ?: error("no image")
                )
            )
            markerTapListener = MapObjectTapListener { _, _ ->
                if (binding.showInfo.visibility == VISIBLE) {
                    binding.showInfo.visibility = GONE
                } else {
                    binding.showInfo.visibility = VISIBLE
                }
                true
            }
            mapObject.addTapListener(markerTapListener)
            setPlacemarkInfo(placemark)
            mapView.map.move(
                CameraPosition(
                    Point(placemark.coordinates.latitude, placemark.coordinates.longitude),
                    14.0f, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
            binding.edit.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("placemark", placemark)
                val editPlacemarkFragment = EditPlacemarkFragment()
                editPlacemarkFragment.show(parentFragmentManager, "TAG")
                editPlacemarkFragment.arguments = bundle
            }
        } else {
            mapView.map.move(
                CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }

        viewModel.data.observe(viewLifecycleOwner) { markers ->
            binding.showList.setOnClickListener {
                if (markers.isNotEmpty()) {
                    findNavController().navigate(
                        R.id.action_mapFragment_to_placesListFragment
                    )
                } else {
                    Toast.makeText(
                        activity,
                        this.getString(R.string.list_is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding.showOnMap.setOnClickListener {
                if (markers.isNotEmpty()) {
                    for (marker in markers) {
                        val mapObject = collection.addPlacemark(
                            Point(marker.coordinates.latitude, marker.coordinates.longitude),
                            ImageProvider.fromBitmap(
                                context?.let {
                                    AppCompatResources.getDrawable(
                                        it,
                                        R.drawable.ic_baseline_my_location_48
                                    )?.toBitmap()
                                } ?: error("no image")
                            )
                        )
                        markerTapListener = MapObjectTapListener { _, _ ->
                            binding.edit.setOnClickListener {
                                val bundle = Bundle()
                                bundle.putParcelable("placemark", marker)
                                val editPlacemarkFragment = EditPlacemarkFragment()
                                editPlacemarkFragment.show(parentFragmentManager, "TAG")
                                editPlacemarkFragment.arguments = bundle
                            }
                            setPlacemarkInfo(marker)
                            binding.showInfo.visibility = VISIBLE
                            true
                        }
                        mapObject.addTapListener(markerTapListener)
                    }

                    val boundingBox = BoundingBox(
                        Point(
                            markers.maxOfOrNull { it.coordinates.latitude }!!,
                            markers.maxOfOrNull { it.coordinates.longitude }!!
                        ),
                        Point(
                            markers.minOfOrNull { it.coordinates.latitude }!!,
                            markers.minOfOrNull { it.coordinates.longitude }!!
                        )
                    )

                    val cameraPosition = mapView.map.cameraPosition(boundingBox)

                    mapView.map.move(
                        CameraPosition(
                            cameraPosition.target,
                            cameraPosition.zoom - 1f,
                            0f,
                            0F
                        ),
                        Animation(Animation.Type.SMOOTH, 1f),
                        null
                    )
                } else {
                    Toast.makeText(
                        activity,
                        this.getString(R.string.list_is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onMapTap(p0: Map, p1: Point) {
        print("point: " + p1.latitude + ", " + p1.longitude)
        if (binding.showInfo.visibility == VISIBLE) {
            binding.showInfo.visibility = GONE
        }
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        val bundle = Bundle()
        bundle.putDouble("latitude", p1.latitude)
        bundle.putDouble("longitude", p1.longitude)
        val newPlacemarkFragment = NewPlacemarkFragment()
        newPlacemarkFragment.show(parentFragmentManager, "TAG")
        newPlacemarkFragment.arguments = bundle
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.83).toFloat())
        )

        mapView.getMap().move(
            CameraPosition(
                Point(mapView.getWidth().toDouble(), mapView.getHeight().toDouble()),
                14f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )

        userLocationView.accuracyCircle.fillColor = Color.YELLOW
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    fun setPlacemarkInfo(placemark: Placemark){
        binding.name.setText(placemark.name)
        binding.description.setText(placemark.description)
        val text =
            "Широта ${placemark.coordinates.latitude}, \nДолгота ${placemark.coordinates.longitude}."
        binding.coords.setText(text)
    }
}