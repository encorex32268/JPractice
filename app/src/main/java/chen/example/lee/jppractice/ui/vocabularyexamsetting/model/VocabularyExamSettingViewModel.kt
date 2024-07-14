package chen.example.lee.jppractice.ui.vocabularyexamsetting.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chen.example.lee.jppractice.db.model.Vocabulary
import chen.example.lee.jppractice.db.room.VocabularyDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class VocabularyExamSettingViewModel @Inject constructor(
    val vocabularyDao: VocabularyDao
) : ViewModel(){

    var state = MutableStateFlow(VocabularyExamSettingState())

    fun onEvent(event : VocabularyExamSettingEvent){
        viewModelScope.launch {
            when(event){
                is VocabularyExamSettingEvent.GetDataToExam ->{
                    var result = arrayListOf<Vocabulary>()
                    vocabularyDao.getAll().collect {
                        it.forEach { vocabulary->
                            event.types.forEach {  type->
                                if (vocabulary.type == type){
                                    result.add(vocabulary)
                                }
                            }
                        }
                        state.value = state.value.copy(
                            result
                        )
                    }
                }
            }
        }
    }

    }

