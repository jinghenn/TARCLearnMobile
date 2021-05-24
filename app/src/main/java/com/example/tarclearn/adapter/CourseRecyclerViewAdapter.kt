package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.UserCourseDto

class CourseRecyclerViewAdapter() : RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>() {

    var courseList = listOf<UserCourseDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardHeader: TextView = itemView.findViewById(R.id.tv_card_header)
        val cardSubHeader: TextView = itemView.findViewById(R.id.tv_card_subheader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.course_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = courseList[position]
        holder.cardHeader.text = item.courseId
        holder.cardSubHeader.text = item.courseName
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}