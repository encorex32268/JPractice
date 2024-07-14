package chen.example.lee.jppractice.ui.home.model

import chen.example.lee.jppractice.db.model.JPVoice

sealed class HomeEvent {
    data class JPVoiceChanged(val jpVoice: JPVoice) : HomeEvent()
    data class ToReverse(val isReverse : Boolean) : HomeEvent()
}