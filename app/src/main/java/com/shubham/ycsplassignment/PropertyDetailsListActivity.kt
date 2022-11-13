package com.shubham.ycsplassignment

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shubham.ycsplassignment.adapter.RecyclerViewAdapter
import com.shubham.ycsplassignment.db.PropertyDetailsDatabase
import com.shubham.ycsplassignment.repository.PropertyRepository
import com.shubham.ycsplassignment.utils.Helper

class PropertyDetailsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details_list)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.recycler_view)
        constraintLayout = findViewById(R.id.constraintLayout)

        recyclerView.layoutManager = LinearLayoutManager(this@PropertyDetailsListActivity)

        val dao = PropertyDetailsDatabase.getInstance(application).propertyDetailsDAO
        val repository = PropertyRepository(dao)

        repository.properties.observe(this) {
            recyclerView.adapter = RecyclerViewAdapter(it) {

            }
        }

    }

}