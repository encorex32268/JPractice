package chen.example.lee.jppractice.ui.vocabularyexam.model

import chen.example.lee.jppractice.db.model.Vocabulary

data class VocabularyExamState(
    var questionCounter : Int = 1,
    var correct : Int = 0 ,
    var wrong : Int = 0 ,
    var question : Vocabulary?=null
)