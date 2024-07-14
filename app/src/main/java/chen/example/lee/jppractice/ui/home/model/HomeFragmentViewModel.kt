package chen.example.lee.jppractice.ui.home.model

import androidx.lifecycle.ViewModel
import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.JPVoice
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class HomeFragmentViewModel  @Inject constructor(
    var allWordToneData: AllWordToneData
) : ViewModel(){

    var state  = MutableStateFlow(HomeState(allWordToneData = allWordToneData))
    init {
        state.value.apply {
            gridlayoutCount = 5
            jpVoice = JPVoice.Clear
            allWordToneData = allWordToneData.toClearAllToneData()
            isAllReverse = false
        }

    }

    fun onEvent(event : HomeEvent){
        when(event){
            is HomeEvent.ToReverse->{
                state.value = state.value.copy(isAllReverse = event.isReverse)
            }
            is HomeEvent.JPVoiceChanged->{
                when(event.jpVoice){
                    is JPVoice.Clear->{
                        state.value = state.value.copy(
                            allWordToneData = allWordToneData.toClearAllToneData(),
                            gridlayoutCount = 5,
                            jpVoice = JPVoice.Clear
                        )
                    }
                    is JPVoice.Bend->{
                        state.value = state.value.copy(
                            allWordToneData = allWordToneData.toBendAllToneData(),
                            gridlayoutCount = 3,
                            jpVoice = JPVoice.Bend

                        )
                    }
                    is JPVoice.Foodty->{
                        state.value = state.value.copy(
                            allWordToneData = allWordToneData.toFoodtyAllToneData(),
                            gridlayoutCount = 5,
                            jpVoice = JPVoice.Foodty
                        )
                    }
                }
            }


        }
    }







}