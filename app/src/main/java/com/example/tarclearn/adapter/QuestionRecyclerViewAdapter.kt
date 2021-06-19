package com.example.tarclearn.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.Question

class QuestionRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder>() {
    var questionList = mutableListOf<Question>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val radioGroupList = mutableListOf<RadioGroup>()
    private val qtnLayoutList = mutableListOf<LinearLayout>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tv_question)
        val choiceRadioGroup: RadioGroup = itemView.findViewById(R.id.choice_radio_group)
        val questionLayout: LinearLayout = itemView.findViewById(R.id.question_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_quiz_question, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adPosition = holder.adapterPosition
        val item = questionList[adPosition]

        holder.tvQuestion.text = context.getString(
            R.string.question_text, adPosition + 1, item.questionText
        )

        item.Choices.forEach { c ->
            val radioButton = RadioButton(context)
            radioButton.text = c.choiceText
            holder.choiceRadioGroup.addView(radioButton)

        }
        val firstBtn = holder.choiceRadioGroup.getChildAt(0) as RadioButton
        firstBtn.isChecked = true
        radioGroupList.add(holder.choiceRadioGroup)
        qtnLayoutList.add(holder.questionLayout)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    fun checkAnswer(): Int {
        var score = 0
        radioGroupList.forEachIndexed { i, radioGroup ->
            for ((j, b) in radioGroup.children.withIndex()) {
                val btn = b as RadioButton
                if (btn.isChecked && questionList[i].Choices[j].isAnswer) {
                    score += 1
                    qtnLayoutList[i].setBackgroundColor(Color.parseColor("#98FB98"))
                    break
                } else {
                    qtnLayoutList[i].setBackgroundColor(Color.parseColor("#FFCCCC"))
                }
            }
        }

        return score
    }


}