package com.shubham.ycsplassignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PropertyDetails::class],
    version = 1
)
abstract class PropertyDetailsDatabase : RoomDatabase() {

    abstract val propertyDetailsDAO: PropertyDetailsDAO

    companion object {

        @Volatile
        private var INSTANCE: PropertyDetailsDatabase? = null

        fun getInstance(context: Context): PropertyDetailsDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PropertyDetailsDatabase::class.java,
                        "property_details_database"
                    ).build()

                }

                return instance
            }
        }

    }


}