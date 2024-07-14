package chen.example.lee.jppractice.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import chen.example.lee.jppractice.db.model.Vocabulary

@Database(entities = [Vocabulary::class] , version = 11,exportSchema=false)
abstract class VocabularyDatabase : RoomDatabase(){
    abstract fun vocabularyDao() : VocabularyDao
}