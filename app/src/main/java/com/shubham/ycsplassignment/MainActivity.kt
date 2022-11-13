package com.shubham.ycsplassignment

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.shubham.ycsplassignment.databinding.ActivityMainBinding
import com.shubham.ycsplassignment.db.PropertyDetailsDatabase
import com.shubham.ycsplassignment.repository.PropertyRepository
import com.shubham.ycsplassignment.utils.Helper
import com.shubham.ycsplassignment.viewmodel.PropertyDetailsViewModel
import com.shubham.ycsplassignment.viewmodel.PropertyDetailsViewModelFactory


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var selectLocationFab: FloatingActionButton
    private lateinit var containerPropertyDetailsForm: ConstraintLayout
    private lateinit var locationIcon: ImageView

    private lateinit var viewModel: PropertyDetailsViewModel
    private lateinit var viewModelFactory: PropertyDetailsViewModelFactory
    private lateinit var binding: ActivityMainBinding

    private lateinit var map: GoogleMap
    private var isMapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val size = Helper.getSoftButtonsBarSizePort(this)
        binding.constraintLayout.setPadding(0, 0, 0, size)


        val dao = PropertyDetailsDatabase.getInstance(application).propertyDetailsDAO
        val repository = PropertyRepository(dao)

        viewModelFactory = PropertyDetailsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[PropertyDetailsViewModel::class.java]

        binding.lifecycleOwner = this
        binding.propertyViewModel = viewModel

        selectLocationFab = findViewById(R.id.select_location_fab)
        containerPropertyDetailsForm = findViewById(R.id.property_details_form_container)
        locationIcon = findViewById(R.id.location_icon)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        selectLocationFab.setOnClickListener {

            if (viewModel.isPropertyFormVisibleVal.value!!) {

                map.clear()
                viewModel.setLatLong(LatLng(0.0, 0.0))
                viewModel.setPropertyName("")

            } else {

                map.addMarker(MarkerOptions().position(map.cameraPosition.target).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.location_icon)))
                viewModel.setLatLong(
                    LatLng(
                        map.cameraPosition.target.latitude,
                        map.cameraPosition.target.longitude
                    )
                )
            }

            updateStatus()
        }

        viewModel.message.observe(this) {

            it?.let {
                if (it.status) {

                    if (isMapReady) {
                        map.clear()
                    }

                    Snackbar.make(binding.constraintLayout, it.message, Snackbar.LENGTH_LONG)
                        .setAction("View") {
                            startActivity(Intent(this, PropertyDetailsListActivity::class.java))
                        }
                        .setActionTextColor(resources.getColor(android.R.color.holo_green_light))
                        .show()

                    viewModel.setMessage(null)

                } else {

                    Snackbar.make(binding.constraintLayout, it.message, Snackbar.LENGTH_LONG)
                        .setActionTextColor(resources.getColor(android.R.color.holo_red_dark))
                        .show()

                    viewModel.setMessage(null)
                }
            }
        }

        viewModel.isPropertyFormVisibleVal.observe(this) {

            if (it) {

                containerPropertyDetailsForm.visibility = View.VISIBLE
                selectLocationFab.setImageResource(R.drawable.clear_icon)
                if (viewModel.latLongVal.value?.latitude == 0.0) {

                    locationIcon.visibility = View.VISIBLE
                    binding.coordinatesEdtx.setText("")

                } else {

                    locationIcon.visibility = View.GONE
                    val latLngStr =
                        "${viewModel.latLongVal.value?.latitude}, ${viewModel.latLongVal.value?.longitude}"
                    binding.coordinatesEdtx.setText(latLngStr)
                }
            } else {
                containerPropertyDetailsForm.visibility = View.GONE
                selectLocationFab.setImageResource(R.drawable.ic_plus_icon)
                if (viewModel.latLongVal.value?.latitude == 0.0) {

                    locationIcon.visibility = View.VISIBLE
                    binding.coordinatesEdtx.setText("")

                } else {

                    locationIcon.visibility = View.GONE
                    val latLngStr =
                        "${viewModel.latLongVal.value?.latitude}, ${viewModel.latLongVal.value?.longitude}"
                    binding.coordinatesEdtx.setText(latLngStr)
                }
            }

            val orientation = resources.configuration.orientation

            val size = Helper.getSoftButtonsBarSizePort(this)

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                binding.constraintLayout.setPadding(0, 0, size, 0)

            } else if (orientation == Configuration.ORIENTATION_PORTRAIT){

                binding.constraintLayout.setPadding(0, 0, 0, size)
            }
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {

        var lastZoomVal: Float = viewModel.cameraZoomVal.value!!

        isMapReady = true

        map = googleMap

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        val delhi = LatLng(28.609054, 77.095770)
        map.moveCamera(CameraUpdateFactory.newLatLng(delhi))
        map.animateCamera(CameraUpdateFactory.zoomTo(lastZoomVal))

        map.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener {
            viewModel.setCameraZoom(it.zoom)
        })

        viewModel.latLongVal.value?.let { ltlg ->

            if (ltlg.latitude != 0.0 && ltlg.longitude != 0.0) {
                map.addMarker(MarkerOptions().position(ltlg).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.location_icon)))

                map.moveCamera(CameraUpdateFactory.newLatLng(ltlg))
            }

        }
    }

    private fun updateStatus() {

        if (viewModel.isPropertyFormVisibleVal.value!!) {

            containerPropertyDetailsForm.visibility = View.VISIBLE

            if (viewModel.latLongVal.value?.latitude == 0.0) {

                locationIcon.visibility = View.VISIBLE
                binding.coordinatesEdtx.setText("")

            } else {

                locationIcon.visibility = View.GONE
                val latLngStr =
                    "${viewModel.latLongVal.value?.latitude}, ${viewModel.latLongVal.value?.longitude}"
                binding.coordinatesEdtx.setText(latLngStr)
            }

            selectLocationFab.setImageResource(R.drawable.clear_icon)
            viewModel.changeVisibility(false)

        } else {

            containerPropertyDetailsForm.visibility = View.GONE
            if (viewModel.latLongVal.value?.latitude == 0.0) {

                locationIcon.visibility = View.VISIBLE
                binding.coordinatesEdtx.setText("")

            } else {

                locationIcon.visibility = View.GONE
                val latLngStr =
                    "${viewModel.latLongVal.value?.latitude}, ${viewModel.latLongVal.value?.longitude}"
                binding.coordinatesEdtx.setText(latLngStr)
            }
            selectLocationFab.setImageResource(R.drawable.ic_plus_icon)
            viewModel.changeVisibility(true)
        }

    }


}