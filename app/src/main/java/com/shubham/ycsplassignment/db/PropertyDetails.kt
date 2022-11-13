package com.shubham.ycsplassignment.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "property_details")
data class PropertyDetails(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    val id: Int,

    @ColumnInfo(name = "propertyName")
    var propertyName: String,

    @ColumnInfo(name = "propertyLatitude")
    var propertyLatitude: Double,

    @ColumnInfo(name = "propertyLongitude")
    var propertyLongitude: Double
)
