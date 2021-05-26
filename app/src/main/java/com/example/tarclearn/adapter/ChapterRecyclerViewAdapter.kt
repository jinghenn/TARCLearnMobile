package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.UserCourseDto
import com.example.tarclearn.ui.fragment.CourseListFragmentDirections
import com.google.android.material.card.MaterialCardView

class ChapterRecyclerViewAdapter() : RecyclerView.Adapter<ChapterRecyclerViewAdapter.ViewHolder>() {
    var chapterList = listOf<UserCourseDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.course_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chapterList[position]

    }

    override fun getItemCount(): Int {
        return chapterList.size
    }
}