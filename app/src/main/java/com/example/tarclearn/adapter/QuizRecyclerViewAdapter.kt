package com.example.tarclearn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.QuizDto
import com.example.tarclearn.repository.ChapterRepository
import com.example.tarclearn.ui.quiz.QuizListFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizRecyclerViewAdapter : RecyclerView.Adapter<QuizRecyclerViewAdapter.ViewHolder>() {
    var quizList = mutableListOf<QuizDto>()
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
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_editable_card, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = quizList[position]
        holder.title.text = item.quizTitle
        holder.card.setOnClickListener{
            val action = QuizListFragmentDirections.actionQuizListFragmentToQuizFragment(item.quizId)
            holder.card.findNavController().navigate(action)
        }
        holder.deleteBtn.setOnClickListener {
            MaterialAlertDialogBuilder(holder.deleteBtn.context)
                .setTitle("Remove Chapter")
                .setMessage("Are you sure you want to remove this chapter?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
//                        val repository = ChapterRepository()
//                        val response = repository.deleteQuiz(item.quizId)
//                        if (response.code() == 200) {
//                            quizList.remove(item)
//                            notifyDataSetChanged()
//                        }
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
    }

    override fun getItemCount(): Int {
        return quizList.size
    }
}