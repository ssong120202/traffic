package com.example.myapplication.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.extension.load
import com.example.myapplication.model.TrafficInfo
import kotlinx.android.synthetic.main.traffic_view_cell.view.*


class TrafficAdapter(private val trafficInfoList: List<TrafficInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TrafficViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrafficViewHolder) {
            holder.bind(trafficInfoList[position])
        }
    }

    override fun getItemCount(): Int {
        return trafficInfoList.size
    }

}

class TrafficViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.traffic_view_cell, parent, false)) {
    private var locationText: TextView = itemView.camera_location_text_view
    private var trafficImage: ImageView = itemView.traffic_image_view

    fun bind(trafficInfo: TrafficInfo) {
        locationText.text = trafficInfo.camera_location
        try {
            trafficImage.load(_url = trafficInfo.cameraUrl.url)
        } catch (e: Exception) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
    }
}

