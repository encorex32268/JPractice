package chen.example.lee.jppractice.ui.practice.model

import chen.example.lee.jppractice.db.model.*

data class PracticeState(
    var counter : Int = 0 ,
    var correct : Int = 0,
    var wrong : Int = 0,
    var practiceQuestion: PracticeQuestion = PracticeQuestion(arrayListOf()),
    var mPracticeType : PracticeType = PracticeType(
        saveWord = JPWord.All,
        saveCount = 5,
        saveToneInt = JPVoice.Clear
    )
)