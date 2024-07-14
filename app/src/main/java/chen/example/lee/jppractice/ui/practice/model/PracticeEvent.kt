package chen.example.lee.jppractice.ui.practice.model

import chen.example.lee.jppractice.db.model.PracticeQuestion
import chen.example.lee.jppractice.db.model.PracticeType

sealed class PracticeEvent  {
    data class CreatePracticeData(val practiceType: PracticeType) : PracticeEvent()
    object NextQuestion : PracticeEvent()
    object CorrectPlus : PracticeEvent()
    object WrongPlus : PracticeEvent()
    data class RemoveQuestionFromQuestionList(val practiceQuestion: PracticeQuestion) : PracticeEvent()

}