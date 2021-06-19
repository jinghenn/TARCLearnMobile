package com.example.tarclearn.model

data class QuizDto(
    val quizId: Int,
    val quizTitle: String
)
data class QuizQuestionsDto(
    val quizId: Int,
    val quizTitle: String,
    val questions: List<QuestionDto>
){
    fun getQuestionList(): MutableList<Question>{
        val questionList = mutableListOf<Question>()
        questions.forEach{ q ->
            val currentChoiceList = mutableListOf<Choice>()
            q.choices.forEach { c ->
                currentChoiceList.add(Choice(c.choiceText, c.isAnswer))
            }
            questionList.add(Question(q.questionText, currentChoiceList))
        }
        return questionList
    }
}
data class Quiz(
    val quizTitle: String,
    val Questions: MutableList<Question>,
    val chapterId: Int
)