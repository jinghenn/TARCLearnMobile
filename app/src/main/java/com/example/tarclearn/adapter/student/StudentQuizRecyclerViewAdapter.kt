package com.example.tarclearn.adapter.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.QuizDto
import com.example.tarclearn.ui.quiz.QuizListFragmentDirections
import com.google.android.material.card.MaterialCardView

class StudentQuizRecyclerViewAdapter :
    RecyclerView.Adapter<StudentQuizRecyclerViewAdapter.ViewHolder>() {
    var quizList = mutableListOf<QuizDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.card_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item_view_card, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = quizList[position]
        holder.title.text = item.quizTitle
        holder.card.setOnClickListener {
            val action =
                QuizListFragmentDirections.actionQuizListFragmentToQuizFragment(item.quizId)
            holder.card.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return quizList.size
    }
}