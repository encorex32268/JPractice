package chen.example.lee.jppractice.db.model

import android.os.Parcelable
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.parcelize.Parcelize

/**
 * Created by Lee on 2017/7/14.
 */

object JPContract {

    const val VOCABULARY_DBNAME = "vocabularyDB"
    const val NUMCOLUMS_NORMAL = 5
    const val NUMCOLUMS_BENDTONE = 3
    const val SHAREDPREFERENCES_DATA_NAME = "mydata"

    /**
     * MainActivity
     */
    const val VERSION_NUMBER = "version_number"

    /**
     * ExamSettingFragment
     */
    const val MIN_VALUE = 1
    const val WORD_ALL_PICK = 3
    const val TONE_FOOTY_PICK = 2
    const val TONE_BEND_PICK = 3
    const val COUNT_PICK = 19

    /**
     * PracticeFragment arguments
     */
    const val RUNNABLE_DELAYED_TIME  = 700L
    const val BOOLEAN_PRACTICEVOICE_KEY = "PRACTICEVOICE_KEY"

    /**
     * HomeFragment
     */
    const val BOOLEAN_VOICE_KEY = "VOICE_KEY"
    /**
     * VocabularyFragment
     */
    const val BOOLEAN_VOICE_VOCABULARY_KEY = "VOICE_VOCABULARY_KEY"
    const val FLIPVIEW_DURATION=400

    /**
     * VocabularyExamFragment
     */
    const val FUKUSHI = "副詞"
    const val IKEIYOUSHI = "い形容詞"
    const val NAKEIYOUSHI = "な形容詞"

    /**
     * VocabularyFragmentViewModel
     */
    const val RESOURCE_ERROR_NO_DATA = "No Data"
    const val RESOURCE_ERROR_TYPE_IS_EMPTY = "Type is empty"

    fun getTypeChinese(type : String ) : String {
        return when(type){
            "动词"->{ "動詞" }
            "副词"->{ "副詞" }
            "い形容词"->{ "い形容詞" }
            "名词"->{ "名詞" }
            "な形容词"->{ "な形容詞" }
            else -> {type}
        }
    }
}

