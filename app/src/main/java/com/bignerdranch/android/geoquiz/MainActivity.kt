package com.bignerdranch.android.geoquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel : QuizViewModel by viewModels()

    enum class QuestionDirection {
        PREVIOUS,
        NEXT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.questionTextView.setOnClickListener { view: View ->
            updateQuestionIndexAndQuestion(QuestionDirection.NEXT)
        }

        binding.trueButton.setOnClickListener { view: View ->

            checkAnswer(true, view)
        }

        binding.falseButton.setOnClickListener { view: View ->

            checkAnswer(false, view)
        }

        binding.previousButton.setOnClickListener { view: View ->
            updateQuestionIndexAndQuestion(QuestionDirection.PREVIOUS)
        }

        binding.nextButton.setOnClickListener { view: View ->

            updateQuestionIndexAndQuestion(QuestionDirection.NEXT)

        }

        binding.cheatButton.setOnClickListener { view: View ->
            val cheatIntent = Intent(this, CheatActivity::class.java)
            startActivity(cheatIntent)

        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart(Bundle?) called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume(Bundle?) called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause(Bundle?) called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(Bundle?) called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy(Bundle?) called")
    }


    private fun updateQuestionIndexAndQuestion(questionDirection: QuestionDirection) {

        quizViewModel.moveToNext(questionDirection)
        updateQuestion()
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestion.textResId
        binding.questionTextView.setText(questionTextResId)

        updateButtons()
    }

    private fun updateButtons() {
        val currentQuestion = getCurrentQuestion()

        if (currentQuestion.answerState == UserAnswerState.ANSWERED) {
            disableAnswerButtons()
        } else {
            enableAnswerButtons()
        }
    }
    private fun getCurrentQuestion(): Question {
        return quizViewModel.currentQuestion
    }

    private fun disableAnswerButtons() {
        binding.trueButton.isEnabled = false
        binding.trueButton.isClickable = false
        binding.falseButton.isEnabled = false
        binding.falseButton.isClickable = false
    }

    private fun enableAnswerButtons() {
        binding.trueButton.isEnabled = true
        binding.trueButton.isClickable = true
        binding.falseButton.isEnabled = true
        binding.falseButton.isClickable = true
    }

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val currentQuestion = getCurrentQuestion()

        val correctAnswer = quizViewModel.currentQuestion.correctAnswer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        currentQuestion.userAnswer = userAnswer
        currentQuestion.answerState = UserAnswerState.ANSWERED
        updateButtons()

        Snackbar.make(
            view,
            messageResId,
            Snackbar.LENGTH_SHORT
        ).show()

        quizViewModel.incrementNumberOfQuestionsAnswered()

        if (quizViewModel.totalNumberOfQuestionsAnswered == quizViewModel.totalNumberOfQuestions) {
            val userScore = calculateScore()

            Snackbar.make(
                view,
                "Congrats! Your score is $userScore%",
                Snackbar.LENGTH_INDEFINITE
            ).show()

        }
    }

    private fun calculateScore(): Double {
        var userQuizScore = 0.0

        for (question in quizViewModel.questionBankCopy) {
            val userAnswer = question.userAnswer
            val questionAnswer = question.correctAnswer

            if (userAnswer == questionAnswer) {
                userQuizScore += 1.0
            }
        }

        return (userQuizScore / quizViewModel.totalNumberOfQuestions) * 100.0
    }
}
