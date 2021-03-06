package com.example.tarclearn.adapter.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.MaterialDetailDto
import com.example.tarclearn.ui.video.VideoListFragmentDirections
import com.google.android.material.card.MaterialCardView

class StudentVideoRecyclerViewAdapter() :
    RecyclerView.Adapter<StudentVideoRecyclerViewAdapter.ViewHolder>() {
    var videoList = mutableListOf<MaterialDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.card_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.student_item_view_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = videoList[position]
        holder.title.text = holder.title.context.getString(R.string.material_title, item.index, item.materialTitle)
        holder.card.setOnClickListener {
            val action =
                VideoListFragmentDirections.actionVideoListFragmentToVideoFragment(item.materialId)
            holder.card.findNavController().navigate(action)
        }
    }
}