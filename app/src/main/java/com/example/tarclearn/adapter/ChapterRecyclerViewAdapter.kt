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
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.ui.activity.ChapterActivity
import com.example.tarclearn.ui.course.CourseInfoFragmentDirections
import com.example.tarclearn.util.Constants
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapterRecyclerViewAdapter(val courseId: Int) :
    RecyclerView.Adapter<ChapterRecyclerViewAdapter.ViewHolder>() {
    var chapterList = mutableListOf<ChapterDetailDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.card_text)
        val editBtn: Button = itemView.findViewById(R.id.btn_edit)
        val deleteBtn: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_editable_card, parent, false)
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
        holder.deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(holder.deleteBtn.context)
                .setTitle("Remove Chapter")
                .setMessage("Are you sure you want to remove this chapter?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val repository = ChapterRepository()
                        val response = repository.deleteChapter(item.chapterId)
                        if (response.code() == 200) {
                            chapterList.remove(item)
                            notifyDataSetChanged()
                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
        holder.editBtn.setOnClickListener {
            val action =
                CourseInfoFragmentDirections.actionCourseInfoFragmentToManageChapterFragment(
                    item.chapterId,
                    Constants.MODE_EDIT,
                    courseId
                )
            holder.editBtn.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }
}