package chen.example.lee.jppractice.ui.home.model

import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.JPVoice

data class HomeState(
    var gridlayoutCount : Int = 5,
    var jpVoice: JPVoice = JPVoice.Clear,
    var allWordToneData: AllWordToneData,
    var isAllReverse : Boolean = false
)