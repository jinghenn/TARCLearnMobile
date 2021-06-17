package com.example.tarclearn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.ChoiceDto
import com.example.tarclearn.model.QuestionDto
import org.w3c.dom.Text

class QuestionRecyclerViewAdapter : RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder>() {
    var questionList = mutableListOf<QuestionDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val radioGroupList = mutableListOf<RadioGroup>()
    private val answerList = mutableListOf<Int>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.tv_question)
        val choiceLayout: LinearLayout = itemView.findViewById(R.id.choice_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_quiz_question, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.question.context
        val item = questionList[position]
        holder.question.text =
            context.getString(R.string.question_text, (position + 1), item.questionText)

        initializeChoices(context, holder.choiceLayout, item.choices)

    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    private fun initializeChoices(context: Context, parent: ViewGroup, choices: List<ChoiceDto>) {
        val child = LayoutInflater.from(context)
            .inflate(R.layout.choice, parent, true)
        val radioGroup = child.findViewById<RadioGroup>(R.id.choice_radio_group)
        choices.forEachIndexed { idx, c ->
            val radioBtn = RadioButton(context)
            radioBtn.text = c.choiceText
            radioBtn.id = c.choiceId
            if(c.isAnswer){
                answerList.add(c.choiceId)
            }
            radioGroup.addView(radioBtn)
            if(idx == 0){
                radioGroup.check(radioBtn.id)
            }
        }

        radioGroupList.add(radioGroup)
    }

    fun checkAnswer(): Int {
        var result = 0

        radioGroupList.forEachIndexed { idx, grp ->
            grp.children.forEach { v ->
                if(v is RadioButton){
                    v.error = null
                }
            }
            val answerBtn = grp.findViewById<RadioButton>(answerList[idx])
            val selectedBtn = grp.findViewById<RadioButton>(grp.checkedRadioButtonId)
            val errorView = TextView(grp.context)
            errorView.id = answerList[idx] + 1000
            if(selectedBtn.id == -1 ){
                return -1
            }
            if(selectedBtn.id == answerList[idx]){

                result++
                val x = grp.findViewById<TextView>(answerList[idx] + 1000)
                x?.let{
                    grp.removeView(it)
                }

            }else{
                selectedBtn.error = "Incorrect"
                val x = grp.findViewById<TextView>(answerList[idx] + 1000)
                if(x == null){
                    errorView.text = answerBtn.context.getString(R.string.answer, answerBtn.text)
                    grp.addView(errorView)
                }
            }

        }
        return result
    }
}