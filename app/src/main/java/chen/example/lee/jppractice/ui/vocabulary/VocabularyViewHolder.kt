package chen.example.lee.jppractice.ui.vocabulary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import chen.example.lee.jppractice.db.model.JPContract.FLIPVIEW_DURATION
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.db.model.Vocabulary
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.VocabularyItemBackBinding
import com.example.lee.jppractice.databinding.VocabularyItemBinding
import com.example.lee.jppractice.databinding.VocabularyItemFrontBinding
import com.wajahatkarim3.easyflipview.EasyFlipView

class VocabularyViewHolder(
    itemView: View,
    private val soundHandler: SoundHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository

) : RecyclerView.ViewHolder(itemView){

    private val bindingFont = VocabularyItemFrontBinding.bind(itemView)
    private val bindingBack = VocabularyItemBackBinding.bind(itemView)
    private val binding = VocabularyItemBinding.bind(itemView)

    fun bindTo(vocabulary: Vocabulary){
        binding.apply {
            if (easyFlipView.currentFlipState == EasyFlipView.FlipState.BACK_SIDE && !(vocabulary.isFliped)){
                easyFlipView.flipDuration = 0
                easyFlipView.flipTheView()
            }else if(easyFlipView.currentFlipState == EasyFlipView.FlipState.FRONT_SIDE && vocabulary.isFliped){
                easyFlipView.flipDuration = 0
                easyFlipView.flipTheView()
            }
        }
        bindingFont.apply {
            if (vocabulary.word.isEmpty()){
                vocabularyFuriganaView.setText(vocabulary.hiragana)
            }else{
                vocabularyFuriganaView.setText("{${vocabulary.word};${vocabulary.hiragana}}")
            }
            vocabularyWordExplain.text = vocabulary.wordExplain
            if (vocabulary.example.isEmpty()){
                turnImageView.visibility = View.GONE
            }else{
                turnImageView.setOnClickListener {
                    binding.apply {
                        easyFlipView.flipDuration = FLIPVIEW_DURATION
                        easyFlipView.flipTheView()
                    }
                    vocabulary.isFliped =true
                    if (sharedPreferencesRepository.getVocabularyVoiceBoolean()){
                        soundHandler.soundVocabulary(vocabulary.examplePlayResourceName)
                    }
                }
            }
            vocabularySound.setOnClickListener {
                if (sharedPreferencesRepository.getVocabularyVoiceBoolean()){
                    soundHandler.soundVocabulary(vocabulary.wordPlayResourceName)

                }
            }
        }
        bindingBack.apply {
            if(vocabulary.example.isEmpty() && vocabulary.exampleHiragana.isEmpty()){
                vocabularyExample.setText(itemView.context.resources.getString(R.string.vocabulary_viewholder_withoutexample))
            }else if (vocabulary.example.isEmpty()){
                vocabularyExample.setText(vocabulary.example)
            }
            else{
                vocabularyExample.setText("{${vocabulary.example};${vocabulary.exampleHiragana}}")
            }
            vocabularyExampleExplain.text = vocabulary.exampleTranslate
            turnImageViewBack.setOnClickListener {
                binding.apply {
                    easyFlipView.flipDuration = FLIPVIEW_DURATION
                    easyFlipView.flipTheView()
                }
                vocabulary.isFliped = false
            }
        }



    }




}