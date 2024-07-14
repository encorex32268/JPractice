package chen.example.lee.jppractice.ui.home

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.JPWord
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.AlltoneCustomviewBinding

class HomeViewHolder(
    itemView: View,
    private val soundHandler: SoundHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository

)
    : RecyclerView.ViewHolder(itemView)
{
    private val binding = AlltoneCustomviewBinding.bind(itemView)
    fun bindTo(localData: AllWordToneData, position:Int, isReverse:Boolean){
        itemView.tag = position
        itemView.setOnClickListener {
            itemClick(localData, position)
        }
        val roma = localData.romaDataList[position]
        binding.apply {
            romaTextView.text = specialWord(roma)
            when(isReverse){
                true->{
                    toneTextView.apply {
                        text  = localData.katakanaList[position]
                        setTextColor(ContextCompat.getColor(context,R.color.color_alltone_katakana))
                    }
                }
                false->{
                    toneTextView.apply {
                        text  = localData.hiraganaList[position]
                        setTextColor(ContextCompat.getColor(context,R.color.color_alltone_hiragana))
                    }
                }
            }
        }

    }

    private fun specialWord(roma: String): String {
        var returnValue = roma
        when (returnValue) {
            "zi" -> {
                returnValue += "/ji"
            }
            "di" -> {
                returnValue += "(zi)"
            }
            "du" -> {
                returnValue += "(zu)"
            }
            "dya" -> {
                returnValue += "/ja"
            }
            "dyu" -> {
                returnValue += "/ju"
            }
            "dyo" -> {
                returnValue += "/jo"
            }
        }
        return returnValue
    }

    private fun itemClick(
        localData: AllWordToneData,
        position: Int
    ) {
        binding.apply {
        val click = toneTextView.text == localData.hiraganaList[position]
        if (click) {
            toneTextView.apply {
                text  = localData.katakanaList[position]
                setTextColor(ContextCompat.getColor(context,R.color.color_alltone_katakana))
            }
        } else {
            toneTextView.apply {
                text  = localData.hiraganaList[position]
                setTextColor(ContextCompat.getColor(context,R.color.color_alltone_hiragana))
            }
        }
            if (sharedPreferencesRepository.getHomeVoiceBoolean()) {
                soundHandler.sound(
                    JPWord.Hiragana,
                    localData.jpVoice,
                    binding.romaTextView.text.toString()
                )
            }

        }
    }




}