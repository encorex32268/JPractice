package chen.example.lee.jppractice.repositories

import android.content.Context
import chen.example.lee.jppractice.db.model.JPContract

class SharedPreferencesRepository(
    private val context: Context
) {
    private val mSharedPreferences = context.getSharedPreferences(JPContract.SHAREDPREFERENCES_DATA_NAME, Context.MODE_PRIVATE)

    private fun getBoolean(key : String ) : Boolean{
        return mSharedPreferences.getBoolean(key,false)
    }
    private fun setBoolean(key: String , value : Boolean) {
        mSharedPreferences.edit().putBoolean(key,value).apply()
    }

    private fun getInt(key : String ) : Int {
        return mSharedPreferences.getInt(key,0)
    }
    private fun setInt(key : String , value : Int){
        mSharedPreferences.edit().putInt(key,value).apply()
    }

    //MainActivity
    fun getMainDBVersion() : Int{
        return getInt(JPContract.VERSION_NUMBER)
    }
    fun setMainDBVersion(version : Int){
        setInt(JPContract.VERSION_NUMBER,version)
    }


    //HomeFragment
    fun getHomeVoiceBoolean() : Boolean{
        return getBoolean(JPContract.BOOLEAN_VOICE_KEY)
    }
    fun setHomeVoiceBoolean(isOpenVoice: Boolean) {
        setBoolean(JPContract.BOOLEAN_VOICE_KEY,isOpenVoice)
    }

    //PracticeFragment
    fun getPracticeVoiceBoolean() : Boolean{
        return getBoolean(JPContract.BOOLEAN_PRACTICEVOICE_KEY)
    }
    fun setPracticeVoiceBoolean(isOpenVoice: Boolean) {
        setBoolean(JPContract.BOOLEAN_PRACTICEVOICE_KEY,isOpenVoice)
    }

    //VocabularyFragment
    fun getVocabularyVoiceBoolean() : Boolean{
        return getBoolean(JPContract.BOOLEAN_VOICE_VOCABULARY_KEY)
    }
    fun setVocabularyVoiceBoolean(isOpenVoice: Boolean) {
        setBoolean(JPContract.BOOLEAN_VOICE_VOCABULARY_KEY,isOpenVoice)
    }

}