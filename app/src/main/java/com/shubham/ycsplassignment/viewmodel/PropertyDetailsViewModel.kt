package com.shubham.ycsplassignment.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.shubham.ycsplassignment.db.Event
import com.shubham.ycsplassignment.db.PropertyDetails
import com.shubham.ycsplassignment.repository.PropertyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PropertyDetailsViewModel(private val repository: PropertyRepository) : ViewModel() {

    private var isPropertyFormVisible = MutableLiveData<Boolean>()
    val isPropertyFormVisibleVal: LiveData<Boolean> get() = isPropertyFormVisible

    var propertyName = MutableLiveData<String>()
    val propertyNameVal: LiveData<String> get() = propertyName

    private var latLong = MutableLiveData<LatLng>()
    val latLongVal: LiveData<LatLng> get() = latLong

    private var cameraZoom = MutableLiveData<Float>()
    val cameraZoomVal: LiveData<Float> get() = cameraZoom

    private val statusMessage = MutableLiveData<Event?>()
    val message: LiveData<Event?> get() = statusMessage

    init {
        isPropertyFormVisible.value = false
        propertyName.value = ""
        latLong.value = LatLng(0.0, 0.0)
        cameraZoom.value = 14.834007f
    }

    fun changeVisibility(status: Boolean) {

        isPropertyFormVisible.value = status
    }

    fun setPropertyName(name: String) {

        propertyName.value = name
    }

    fun setLatLong(latLng: LatLng) {

        latLong.value = latLng
    }

    fun setCameraZoom(zoom: Float) {

        cameraZoom.value = zoom
    }

    fun setMessage(event: Event?) {

        statusMessage.value = event
    }

    fun insertPropertyDetails() {

        if(propertyName.value == null || propertyName.value.toString().trim() == "") {

            statusMessage.value = Event("Please enter property name!", false)

        } else if(latLong.value?.latitude == null) {

            statusMessage.value = Event("Please select property location!", false)

        }else if(latLong.value?.longitude == null) {

            statusMessage.value = Event("Please select property location!", false)

        } else if(latLong.value?.latitude == 0.0 || latLong.value?.longitude == 0.0) {

            statusMessage.value = Event("Please select property location!", false)

        } else {

            val propertyNameStr = propertyName.value.toString().trim()
            val propertyLocationLatitude = latLong.value!!.latitude
            val propertyLocationLongitude = latLong.value!!.longitude

            savePropertyDetails(PropertyDetails(0, propertyNameStr, propertyLocationLatitude!!, propertyLocationLongitude))
        }
    }

    fun savePropertyDetails(propertyDetails: PropertyDetails): Job = viewModelScope.launch {

        val newRowId = repository.insert(propertyDetails)

        if(newRowId > -1) {
            clearData()
            statusMessage.value = Event("Property details inserted successfully.", true)
        } else {
            Log.d("SAVE_PROP_LOG", "Failed")
            statusMessage.value = Event("Error occurred while inserting property details!", false)
        }
    }

    fun clearData() {

        propertyName.value = ""
        latLong.value = LatLng(0.0, 0.0)
        isPropertyFormVisible.value = false
    }

}