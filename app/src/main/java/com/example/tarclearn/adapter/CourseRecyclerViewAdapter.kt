package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.CourseDto
import com.example.tarclearn.ui.course.CourseListFragmentDirections
import com.google.android.material.card.MaterialCardView

class CourseRecyclerViewAdapter() : RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>() {

    var courseList = listOf<CourseDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.item_card)
        val cardHeader: TextView = itemView.findViewById(R.id.tv_card_header)
        val cardSubHeader: TextView = itemView.findViewById(R.id.tv_card_subheader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_course_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = courseList[position]
        holder.cardHeader.text = item.courseCode
        holder.cardSubHeader.text = item.courseTitle
        holder.card.setOnClickListener {
            val action =
                CourseListFragmentDirections.actionCourseFragmentToCourseInfoFragment(item.courseId)
            holder.card.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}