package chen.example.lee.jppractice.utils

import android.content.Context
import chen.example.lee.jppractice.db.model.JPLetter
import chen.example.lee.jppractice.db.model.JPVoice
import chen.example.lee.jppractice.db.model.JPWord
import chen.example.lee.jppractice.db.model.PracticeType
import com.example.lee.jppractice.R
import java.util.*
import kotlin.collections.ArrayList

class DataCreatorUtils (private val context: Context)
{
    lateinit var wordData : ArrayList<String>
    lateinit var romaData : ArrayList<String>

     fun createJPPracticeData(practiceType : PracticeType){
        val wordValue = practiceType.saveWord
        val toneValue = practiceType.saveToneInt
        val countValue = practiceType.saveCount
        getDataFromXml(toneValue, wordValue)
        removeEmptyData()
        if (countValue != 0 && toneValue == JPVoice.Clear){
            var rangeStart = 0
            var rangeEnd = 0
            when(countValue){
                10 ->{ rangeStart = 0 ; rangeEnd = 5 }
                11 ->{ rangeStart = 5 ; rangeEnd = 10 }
                12 ->{ rangeStart = 10; rangeEnd = 15 }
                13 ->{ rangeStart = 15; rangeEnd = 20 }
                14 ->{ rangeStart = 20; rangeEnd = 25 }
                15 ->{ rangeStart = 25; rangeEnd = 30 }
                16 ->{ rangeStart = 30; rangeEnd = 35 }
                17 ->{ rangeStart = 35; rangeEnd = 40 }
                18 ->{ rangeStart = 40; rangeEnd = 46 }
            }
            if (countValue >= 10){
                wordData  =  ArrayList(wordData.subList(rangeStart,rangeEnd))
                romaData = ArrayList(romaData.subList(rangeStart,rangeEnd))
            }else{
                val rangeEnd  = if(countValue == 9) countValue *5 +1 else countValue*5
                wordData  =  ArrayList(wordData.subList(0,rangeEnd))
                romaData= ArrayList(romaData.subList(0,rangeEnd))
            }

        }
    }


    private fun removeEmptyData() {
        val record = arrayListOf<Int>()
        wordData.forEachIndexed { i, s ->
            if (s.isEmpty() || s.isBlank()) {
                record.add(i)
            }
        }
        record.reverse()
        record.forEach {
            wordData.removeAt(it)
        }
        val record2 = arrayListOf<Int>()
        romaData.forEachIndexed { i, s ->
            if (s.isEmpty() || s.isBlank()) {
                record2.add(i)
            }
        }
        record2.reverse()
        record2.forEach {
           romaData.removeAt(it)
        }
    }

    private fun getDataFromXml(jpVoice: JPVoice, jpWord: JPWord) {
        when (jpVoice) {
            is JPVoice.Foodty -> {
                wordData =
                    when (jpWord) {
                        is JPWord.Hiragana -> getStringArrayFromResource(R.array.footyVoiceOfHiragana)
                        is JPWord.Katakana -> getStringArrayFromResource(R.array.footyVoiceOfKatakana)
                        is JPWord.All -> arrayListOf()
                    }
                romaData = getStringArrayFromResource(R.array.footyVoiceOfRoma)
            }
            is JPVoice.Bend -> {
                wordData =
                    when (jpWord) {
                        is JPWord.Hiragana -> getStringArrayFromResource(R.array.bendVoiceOfHiragana)
                        is JPWord.Katakana -> getStringArrayFromResource(R.array.bendVoiceOfKatakana)
                        is JPWord.All -> arrayListOf()
                    }
                romaData = getStringArrayFromResource(R.array.bendVoiceOfRoma)
            }
            is JPVoice.Clear -> {
                wordData =
                    when (jpWord) {
                        is JPWord.Hiragana -> getStringArrayFromResource(R.array.clearVoiceOfHiragana)
                        is JPWord.Katakana -> getStringArrayFromResource(R.array.clearVoiceOfKatakana)
                        is JPWord.All -> arrayListOf()
                    }
                romaData = getStringArrayFromResource(R.array.clearVoiceOfRoma)
            }
        }
    }

    private fun getStringArrayFromResource(rid:Int) : ArrayList<String>{
        return  ArrayList((context.resources.getStringArray(rid)).toMutableList())

    }



    fun getJPLetterList():ArrayList<JPLetter>{
        val result = ArrayList<JPLetter>()
        for(i in wordData.indices){
            val jpLetter = JPLetter(wordData[i], romaData[i])
            result.add(jpLetter)
        }
        return result
    }
    /**
     * For Match Answer
     */
    fun getJPHashMap():HashMap<String,String>{
        val result = HashMap<String,String>()
        for (i in wordData.indices){
            val jpLetter = JPLetter(wordData[i], romaData[i])
            result[jpLetter.roma] = jpLetter.word
        }
        return result
    }



}

