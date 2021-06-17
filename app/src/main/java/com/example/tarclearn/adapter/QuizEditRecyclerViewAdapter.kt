package com.example.tarclearn.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView
import com.example.tarclearn.R
import com.example.tarclearn.model.Choice
import com.example.tarclearn.model.Question

class QuizEditRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<QuizEditRecyclerViewAdapter.ViewHolder>() {
    private val questionTextList = mutableListOf("Question")
    private val etLayoutList = mutableListOf<LinearLayout>()
    private val radioGroupList = mutableListOf<RadioGroup>()
    private val questionList = mutableListOf<Question>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etQuestion: EditText = itemView.findViewById(R.id.et_question)
        val etChoiceLayout: LinearLayout = itemView.findViewById(R.id.et_choice_layout)
        val choiceRadioGroup: RadioGroup = itemView.findViewById(R.id.choice_radio_group)
        val addChoiceBtn: Button = itemView.findViewById(R.id.btn_add_choice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_edit_quiz, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        etLayoutList.add(holder.etChoiceLayout)
        radioGroupList.add(holder.choiceRadioGroup)
        val item = questionTextList[position]
        holder.etQuestion.setText(item)
        holder.addChoiceBtn.setOnClickListener {
            //create new view
            val newRadioBtn = RadioButton(context)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            newRadioBtn.layoutParams = params
            val newEditText = EditText(context)

            //add view
            radioGroupList[position].addView(newRadioBtn)
            etLayoutList[position].addView(newEditText)
            if(radioGroupList[position].childCount == 1){
                val btn = radioGroupList[position].getChildAt(0) as RadioButton
                btn.isChecked = true
            }
        }
    }

    override fun getItemCount(): Int {
        return questionTextList.size
    }

    fun addNewQuestion(){
        questionTextList.add("Question")
        etLayoutList.clear()
        radioGroupList.clear()
        notifyDataSetChanged()
    }

    fun getQuizQuestions(): List<Question>{

        etLayoutList.forEachIndexed { i, it->
            val currentChoiceList = mutableListOf<Choice>()
            it.children.forEachIndexed { j, v ->
                val et = v as EditText
                val radioBtn = radioGroupList[i].getChildAt(j) as RadioButton
                val newChoice = Choice(et.text.toString(), radioBtn.isChecked)
                currentChoiceList.add(newChoice)
            }
            questionList.add(Question(questionTextList[i], currentChoiceList))
        }
        return questionList
    }

}