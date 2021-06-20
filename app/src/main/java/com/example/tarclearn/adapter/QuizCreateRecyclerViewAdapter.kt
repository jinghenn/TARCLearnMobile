package com.example.tarclearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.Choice
import com.example.tarclearn.model.Question
import com.google.android.material.textfield.TextInputLayout

class QuizCreateRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<QuizCreateRecyclerViewAdapter.ViewHolder>() {
    var questionList = mutableListOf<Question>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etQuestionLayout: TextInputLayout = itemView.findViewById(R.id.et_question_layout)
        val etQuestion: EditText = itemView.findViewById(R.id.et_question)
        val etChoiceLayout: LinearLayout = itemView.findViewById(R.id.et_choice_layout)
        val choiceRadioGroup: RadioGroup = itemView.findViewById(R.id.choice_radio_group)
        val addChoiceBtn: Button = itemView.findViewById(R.id.btn_add_choice)
        val deleteQuestionBtn: Button = itemView.findViewById(R.id.btn_delete_question)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_edit_quiz, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = questionList[holder.adapterPosition]
        holder.etQuestion.setText(item.questionText)
        holder.etQuestion.doAfterTextChanged {
            questionList[holder.adapterPosition].questionText = it.toString()
            if (it.toString() == "") {
                holder.etQuestionLayout.isErrorEnabled = true
                holder.etQuestionLayout.error = "Question cannot be empty"
            } else {
                holder.etQuestionLayout.isErrorEnabled = false
            }
        }
        holder.etChoiceLayout.removeAllViews()
        holder.choiceRadioGroup.removeAllViews()
        for ((i, c) in item.Choices.withIndex()) {
            val radioBtn = RadioButton(context)
            radioBtn.layoutParams = RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            val editText = EditText(context)
            editText.setText(c.choiceText)
            holder.choiceRadioGroup.addView(radioBtn)
            holder.etChoiceLayout.addView(editText)
            if (holder.choiceRadioGroup.childCount == 1) {
                radioBtn.isChecked = true
            }
            if (c.isAnswer) {
                radioBtn.isChecked = true
            }
            editText.doAfterTextChanged {
                questionList[holder.adapterPosition].Choices[i].choiceText = it.toString()
                if (it.toString() == "") {
                    editText.error = "Choice cannot be empty"
                } else {
                    editText.error = null
                }
            }
            radioBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                questionList[holder.adapterPosition].Choices[i].isAnswer = isChecked
            }

        }
        holder.deleteQuestionBtn.setOnClickListener {
            questionList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
        holder.addChoiceBtn.setOnClickListener {
            val newChoice = Choice("New Choice", false)
            item.Choices.add(newChoice)
            notifyItemChanged(holder.adapterPosition)
        }
        if (holder.choiceRadioGroup.childCount < 2) {
            holder.etQuestionLayout.isErrorEnabled = true
            holder.etQuestionLayout.error = "Insufficient choice"
        }


    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    fun addQuestion() {
        val newChoice = Choice("New Choice", true)
        val newQuestion = Question("New Question", mutableListOf(newChoice))
        questionList.add(newQuestion)
        //notifyItemInserted(questionList.size)
        notifyDataSetChanged()
    }

    fun getQuizQuestions(): MutableList<Question>? {
        if (questionList.size == 0) {
            return null
        }
        val qIterator = questionList.listIterator()
        while (qIterator.hasNext()) {
            val question = qIterator.next()
            if (isEmptyString(question.questionText)) {
                return null
            }
            val cIterator = question.Choices.listIterator()
            while (cIterator.hasNext()) {

                val choice = cIterator.next()
                if (isEmptyString(choice.choiceText)) {
                    if (question.Choices.size > 1) {
                        cIterator.remove()
                        if (choice.isAnswer) {
                            val next = cIterator.next()
                            next.isAnswer = true
                            cIterator.previous()
                        }
                        notifyDataSetChanged()
                    }
                }
                if (question.Choices.size < 2) {
                    return null
                }
            }
        }

        return questionList
    }

    private fun isEmptyString(s: String): Boolean {
        return s == ""
    }
}