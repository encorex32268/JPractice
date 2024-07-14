package chen.example.lee.jppractice.utils

import chen.example.lee.jppractice.db.model.JPVoice
import chen.example.lee.jppractice.db.model.JPWord

class ToneWordValueUtils {
    companion object{
        fun toneValueCheck(index : Int) : JPVoice {
            return when(index){
                1-> JPVoice.Clear
                2-> JPVoice.Foodty
                else-> JPVoice.Bend
            }

        }
        fun wordValueCheck(index : Int) : JPWord {
            return when(index){
                1-> JPWord.Hiragana
                2-> JPWord.Katakana
                else-> JPWord.All
            }
        }
    }

}