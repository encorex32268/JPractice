package chen.example.lee.jppractice.ui.vocabulary.model

sealed class VocabularyEvent {
    data class TypeChanged(var type : String) : VocabularyEvent()

}