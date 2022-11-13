package com.shubham.ycsplassignment.repository

import com.shubham.ycsplassignment.db.PropertyDetails
import com.shubham.ycsplassignment.db.PropertyDetailsDAO

class PropertyRepository(private val dao: PropertyDetailsDAO) {

    val properties = dao.getAllPropertyDetailsList()

    suspend fun insert(propertyDetails: PropertyDetails) : Long {

        return dao.insertPropertyDetails(propertyDetails)
    }

    suspend fun deleteAll() : Int{

        return dao.deleteAll()
    }

}