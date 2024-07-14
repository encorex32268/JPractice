package chen.example.lee.jppractice.ui.vocabulary.model

import chen.example.lee.jppractice.db.model.Vocabulary

data class VocabularyState(
    val vocabularyList: List<Vocabulary> = listOf()
)