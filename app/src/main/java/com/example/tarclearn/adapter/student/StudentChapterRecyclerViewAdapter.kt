package com.example.tarclearn.adapter.student

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.ChapterDetailDto
import com.example.tarclearn.ui.activity.ChapterActivity
import com.google.android.material.card.MaterialCardView

class StudentChapterRecyclerViewAdapter(val courseId: Int) :
    RecyclerView.Adapter<StudentChapterRecyclerViewAdapter.ViewHolder>() {
    var chapterList = mutableListOf<ChapterDetailDto>()
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chapterList[position]
        val titleText =
            holder.card.context.getString(
                R.string.chapter_title,
                item.chapterNo,
                item.chapterTitle
            )
        holder.title.setText(titleText)
        holder.card.setOnClickListener {
            val context = holder.card.context
            val intent = Intent(context, ChapterActivity::class.java)
            intent.putExtra("chapterId", item.chapterId)
            intent.putExtra("chapterNo", item.chapterNo)
            intent.putExtra("chapterTitle", item.chapterTitle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }
}