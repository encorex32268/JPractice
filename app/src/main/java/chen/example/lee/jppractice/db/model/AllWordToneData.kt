package chen.example.lee.jppractice.db.model
import android.content.Context
import com.example.lee.jppractice.R
import kotlin.collections.ArrayList

data class AllWordToneData(
    val context : Context,
    val romaDataList: ArrayList<String>,
    val hiraganaList: ArrayList<String>,
    val katakanaList: ArrayList<String>
){
    constructor(context: Context):this(context, arrayListOf(), arrayListOf(), arrayListOf())

    lateinit var jpVoice : JPVoice

    fun toClearAllToneData() : AllWordToneData{
        return AllWordToneData(
            context = context,
            romaDataList = getResourcesStringArray(R.array.clearVoiceOfRoma),
            hiraganaList = getResourcesStringArray(R.array.clearVoiceOfHiragana),
            katakanaList = getResourcesStringArray(R.array.clearVoiceOfKatakana)
        ).apply {
            jpVoice = JPVoice.Clear
        }
    }

    fun toFoodtyAllToneData() : AllWordToneData{
        return AllWordToneData(
            context = context,
            romaDataList = getResourcesStringArray(R.array.footyVoiceOfRoma),
            hiraganaList = getResourcesStringArray(R.array.footyVoiceOfHiragana),
            katakanaList = getResourcesStringArray(R.array.footyVoiceOfKatakana)
        ).apply {
            jpVoice = JPVoice.Foodty
        }
    }

    fun toBendAllToneData() : AllWordToneData{
        return AllWordToneData(
            context = context,
            romaDataList = getResourcesStringArray(R.array.bendVoiceOfRoma),
            hiraganaList = getResourcesStringArray(R.array.bendVoiceOfHiragana),
            katakanaList = getResourcesStringArray(R.array.bendVoiceOfKatakana)
        ).apply {
            jpVoice = JPVoice.Bend
        }
    }

    private fun getResourcesStringArray(rid : Int): ArrayList<String> {
        return ArrayList((context.resources.getStringArray(rid)).toMutableList())
    }

}