package com.shubham.ycsplassignment.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PropertyDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPropertyDetails(propertyDetails: PropertyDetails): Long

    @Query("SELECT * FROM property_details")
    fun getAllPropertyDetailsList(): LiveData<List<PropertyDetails>>

    @Query("DELETE FROM property_details")
    suspend fun deleteAll() : Int

}