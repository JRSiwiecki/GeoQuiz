package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.question_mongolia, true),
        Question(R.string.question_brazil, true),
        Question(R.string.question_bermuda_triangle, false),
        Question(R.string.question_largest_island, false),
        Question(R.string.question_mount_rushmore, true)
    )

    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->

            checkAnswer(true, view)
        }

        binding.falseButton.setOnClickListener { view: View ->

            checkAnswer(false, view)
        }

        binding.nextButton.setOnClickListener { view: View ->

            currentQuestionIndex = (currentQuestionIndex + 1) % questionBank.size
            updateQuestion()

        }

        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentQuestionIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean, view: View) {
        val correctAnswer = questionBank[currentQuestionIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Snackbar.make(
            view,
            messageResId,
            3000
        ).show()
    }
}
