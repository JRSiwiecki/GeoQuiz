package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_mongolia, correctAnswer = true, userAnswer = false, answerState = UserAnswerState.UNANSWERED),
        Question(R.string.question_brazil, correctAnswer = true, userAnswer = false, answerState = UserAnswerState.UNANSWERED),
        Question(R.string.question_bermuda_triangle, correctAnswer = false, userAnswer = false, answerState = UserAnswerState.UNANSWERED),
        Question(R.string.question_largest_island, correctAnswer = false, userAnswer = false, answerState = UserAnswerState.UNANSWERED),
        Question(R.string.question_mount_rushmore, correctAnswer = true, userAnswer = false, answerState = UserAnswerState.UNANSWERED)
    )

    private var currentQuestionIndex = 0
    private var numberOfQuestionsAnswered = 0

    val currentQuestion: Question
        get() = questionBank[currentQuestionIndex]

    val totalNumberOfQuestionsAnswered: Int
        get() = numberOfQuestionsAnswered

    val totalNumberOfQuestions: Int
        get() = questionBank.size

    val questionBankCopy: List<Question>
        get() = questionBank

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    fun moveToNext(questionDirection: MainActivity.QuestionDirection) {
        currentQuestionIndex = if (questionDirection == MainActivity.QuestionDirection.PREVIOUS) {
            (currentQuestionIndex - 1 + questionBank.size) % questionBank.size
        } else {
            (currentQuestionIndex + 1) % questionBank.size
        }
    }

    fun incrementNumberOfQuestionsAnswered() {
        numberOfQuestionsAnswered += 1
    }


}