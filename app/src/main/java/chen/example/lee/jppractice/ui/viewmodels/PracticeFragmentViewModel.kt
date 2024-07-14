package chen.example.lee.jppractice.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import chen.example.lee.jppractice.db.model.*
import chen.example.lee.jppractice.ui.practice.model.PracticeEvent
import chen.example.lee.jppractice.ui.practice.model.PracticeState
import chen.example.lee.jppractice.utils.DataCreatorUtils
import chen.example.lee.jppractice.utils.ToneWordValueUtils
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class PracticeFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var jpLetterList :ArrayList<JPLetter>
    private lateinit var lettersHash : HashMap<String, String>
    private var practiceQuestionCount  = 0

    private lateinit var questionList : ArrayList<PracticeQuestion>

    var state = MutableStateFlow(PracticeState())

    fun onEvent(event : PracticeEvent){
       when(event){
           is PracticeEvent.CreatePracticeData->{
               state.value = state.value.copy(
                   mPracticeType =  event.practiceType
               )
               questionList = createQuestionList()
           }
           is PracticeEvent.RemoveQuestionFromQuestionList->{
               if (questionList.size!=0){
                   questionList.remove(event.practiceQuestion)
               }
           }
           is PracticeEvent.NextQuestion->{
               if (questionList.size == 0){
                   questionList = createQuestionList()
               }
               state.value = state.value.copy(
                   practiceQuestion = questionList.random()
               )
           }
           is PracticeEvent.CorrectPlus->{
               var temp = state.value.correct
               temp++
               state.value = state.value.copy(
                   correct = temp
               )
           }
           is PracticeEvent.WrongPlus->{
               var temp = state.value.wrong
               temp++
               state.value = state.value.copy(
                   wrong = temp
               )
           }

       }

    }
    private fun createQuestionList() : ArrayList<PracticeQuestion> {
        when(state.value.mPracticeType.saveWord == JPWord.All){
            true->{
                practiceQuestionCount = 5
                saveDataToList(allPracticePickData())
            }
            false->{
                practiceQuestionCount = 5
                saveDataToList(state.value.mPracticeType)
            }
        }
        val newQuestionList = arrayListOf<PracticeQuestion>()
        for (i in 0..practiceQuestionCount){
            val notSameRandom = HashSet<Int>()
            val jpList = ArrayList<JPLetter>()
            while (notSameRandom.size != 4){
                notSameRandom.add(Random().nextInt(jpLetterList.size))
            }
            notSameRandom.forEach {
                jpList.add(jpLetterList[it])

            }
            val practiceQuestion = PracticeQuestion(jpList)
            newQuestionList.add(practiceQuestion)
        }
        return newQuestionList
    }


    private fun allPracticePickData(): PracticeType {
        val randomWord = Random().nextInt(2) + 1
        val randomTone = Random().nextInt(3) + 1
        val randomCount = 0 // all
        return PracticeType(
            ToneWordValueUtils.wordValueCheck(randomWord),
            ToneWordValueUtils.toneValueCheck(randomTone),
            randomCount
        )
    }

    private fun saveDataToList(practiceType: PracticeType) {
        DataCreatorUtils(getApplication()).apply {
            createJPPracticeData(practiceType)
            jpLetterList = getJPLetterList()
            lettersHash = getJPHashMap()
        }
    }







}