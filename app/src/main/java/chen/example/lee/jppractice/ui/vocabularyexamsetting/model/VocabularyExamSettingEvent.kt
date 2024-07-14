package chen.example.lee.jppractice.ui.vocabularyexamsetting.model

sealed class VocabularyExamSettingEvent {
    data class GetDataToExam(val types : List<String>) : VocabularyExamSettingEvent()
}