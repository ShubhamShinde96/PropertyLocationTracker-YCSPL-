package com.shubham.ycsplassignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubham.ycsplassignment.R
import com.shubham.ycsplassignment.db.PropertyDetails

class RecyclerViewAdapter(private val propertyDetailsList: List<PropertyDetails>, private val clickListener:(PropertyDetails)->Unit): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.property_details_rv_layout, parent, false)

        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(propertyDetailsList[position], clickListener)


    }

    override fun getItemCount(): Int  = propertyDetailsList.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val propertyNameTv: TextView = view.findViewById<TextView>(R.id.property_name_tv)
        private val propertyLocationLatLngTv: TextView = view.findViewById<TextView>(R.id.property_location_latlng_tv)

        fun bind(propertyDetails : PropertyDetails, clickListener:(PropertyDetails)->Unit) {

            propertyNameTv.text = propertyDetails.propertyName

            val locationStr = "${propertyDetails.propertyLatitude}, ${propertyDetails.propertyLongitude}"
            propertyLocationLatLngTv.text = locationStr
        }
    }
}