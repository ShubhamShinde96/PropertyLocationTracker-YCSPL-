package com.shubham.ycsplassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubham.ycsplassignment.repository.PropertyRepository

class PropertyDetailsViewModelFactory(private val propertyRepository: PropertyRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(PropertyDetailsViewModel::class.java)) {

            return PropertyDetailsViewModel(propertyRepository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }
}