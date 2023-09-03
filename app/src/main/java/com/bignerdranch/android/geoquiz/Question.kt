package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

enum class UserAnswerState {
    UNANSWERED,
    ANSWERED
}
data class Question(@StringRes val textResId: Int, val correctAnswer: Boolean, var userAnswer: Boolean, var answerState: UserAnswerState) {

}