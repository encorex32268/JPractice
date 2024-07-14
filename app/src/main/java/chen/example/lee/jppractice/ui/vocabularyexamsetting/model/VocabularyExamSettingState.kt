package chen.example.lee.jppractice.ui.vocabularyexamsetting.model

import chen.example.lee.jppractice.db.model.Vocabulary

data class VocabularyExamSettingState(
    var vocabularyExamData : List<Vocabulary> = emptyList()
)