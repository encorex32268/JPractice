package chen.example.lee.jppractice.ui.vocabularyexam.model

import chen.example.lee.jppractice.db.model.Vocabulary

sealed class VocabularyExamEvent{
    object CounterPlus : VocabularyExamEvent()
    object CorrectPlus : VocabularyExamEvent()
    object WrongPlus : VocabularyExamEvent()
    data class NextQuestion(val question : Vocabulary) : VocabularyExamEvent()
}
