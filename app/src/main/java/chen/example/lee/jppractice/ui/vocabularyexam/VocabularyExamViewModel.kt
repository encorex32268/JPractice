package chen.example.lee.jppractice.ui.vocabularyexam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chen.example.lee.jppractice.ui.vocabularyexam.model.VocabularyExamEvent
import chen.example.lee.jppractice.ui.vocabularyexam.model.VocabularyExamState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VocabularyExamViewModel : ViewModel() {

    var state  = MutableStateFlow(VocabularyExamState())


    fun onEvent(event : VocabularyExamEvent){
        viewModelScope.launch {
            when(event){
                is VocabularyExamEvent.CounterPlus->{
                    var temp = state.value.questionCounter
                    temp++
                    state.value = state.value.copy(
                        questionCounter = temp
                    )
                }
                is VocabularyExamEvent.CorrectPlus->{
                    var temp = state.value.correct
                    temp++
                    state.value = state.value.copy(
                        correct = temp
                    )
                }
                is VocabularyExamEvent.WrongPlus->{
                    var temp = state.value.wrong
                    temp++
                    state.value = state.value.copy(
                        wrong = temp
                    )
                }
                is VocabularyExamEvent.NextQuestion->{
                    state.value = state.value.copy(
                        question =  event.question
                    )
                }

            }


        }
    }

}