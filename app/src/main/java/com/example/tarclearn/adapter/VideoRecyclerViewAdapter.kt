package com.example.tarclearn.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.model.MaterialDto
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.ui.activity.ChapterActivity
import com.example.tarclearn.ui.fragment.CourseInfoFragmentDirections
import com.example.tarclearn.ui.video.VideoListFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoRecyclerViewAdapter() : RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>() {
    var videoList = mutableListOf<MaterialDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.chapter_card)
        val title: TextView = itemView.findViewById(R.id.chapter_title)
        //val editBtn: Button = itemView.findViewById(R.id.btn_edit_chapter)
        //val deleteBtn: Button = itemView.findViewById(R.id.btn_delete_chapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_course_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = videoList[position]
        holder.title.setText(item.materialTitle)
        holder.card.setOnClickListener{
            val action = VideoListFragmentDirections.actionVideoListFragmentToVideoFragment(item.materialId)
            holder.card.findNavController().navigate(action)
        }
    }
}