package chen.example.lee.jppractice.ui.vocabulary.model

import androidx.lifecycle.*
import chen.example.lee.jppractice.db.room.VocabularyDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class VocabularyFragmentViewModel @Inject constructor(
   val vocabularyDao: VocabularyDao
) : ViewModel() {

    var state = MutableStateFlow(VocabularyState())

    fun onEvent(event: VocabularyEvent){
        when(event){
            is VocabularyEvent.TypeChanged ->{
                viewModelScope.launch {
                    vocabularyDao.getTypeData(event.type).collectLatest {
                        state.value = state.value.copy(vocabularyList = it)
                    }
                }

            }
        }
    }




}