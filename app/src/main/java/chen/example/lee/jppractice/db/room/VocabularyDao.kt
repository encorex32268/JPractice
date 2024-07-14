package chen.example.lee.jppractice.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import chen.example.lee.jppractice.db.model.Vocabulary
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {

    @Insert
    fun insertVocabulary(vocabulary: Vocabulary)

    @Query("select * from Vocabulary")
    fun getAll() : Flow<List<Vocabulary>>

    @Query("select * from Vocabulary where type in (:type)")
    fun getTypeData(type : String) : Flow<List<Vocabulary>>


    @Query("DELETE FROM Vocabulary")
    fun deleteAll()
}