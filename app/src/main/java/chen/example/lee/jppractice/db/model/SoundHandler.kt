package chen.example.lee.jppractice.db.model

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.Log
import com.example.lee.jppractice.R

/**
 * Created by Lee on 2017/7/16.
 */

class SoundHandler(private val context:Context){

    private fun returnRomaByRoma(word : String) : Int{
        when(word){
            "a" -> return R.raw.a
            "i" -> return R.raw.i
            "u" -> return R.raw.u
            "e" -> return R.raw.e
            "o" -> return R.raw.o
            "ka" -> return R.raw.ka
            "ki" -> return R.raw.ki
            "ku" -> return R.raw.ku
            "ke" -> return R.raw.ke
            "ko" -> return R.raw.ko
            "sa" -> return R.raw.sa
            "shi" -> return R.raw.shi
            "su" -> return R.raw.su
            "se" -> return R.raw.se
            "so" -> return R.raw.so
            "ta" -> return R.raw.ta
            "chi" -> return R.raw.chi
            "tsu" -> return R.raw.tsuu
            "te" -> return R.raw.te
            "to" -> return R.raw.to
            "na" -> return R.raw.na
            "ni" -> return R.raw.ni
            "nu" -> return R.raw.nu
            "ne" -> return R.raw.ne
            "no" -> return R.raw.no
            "ha" -> return R.raw.ha
            "hi" -> return R.raw.hi
            "fu" -> return R.raw.fu
            "he" -> return R.raw.he
            "ho" -> return R.raw.ho
            "ma" -> return R.raw.ma
            "mi" -> return R.raw.mi
            "mu" -> return R.raw.mu
            "me" -> return R.raw.me
            "mo" -> return R.raw.mo
            "ya" -> return R.raw.ya
            "yu" -> return R.raw.yu
            "yo" -> return R.raw.yo
            "ra" -> return R.raw.ra
            "ri" -> return R.raw.ri
            "ru" -> return R.raw.ru
            "re" -> return R.raw.re
            "ro" -> return R.raw.ro
            "wa" -> return R.raw.wa
            "wo" -> return R.raw.wo
            "n" -> return R.raw.n
            else->{
                return 0
            }
        }
    }
    private fun returnRomaByFoodyBendRoma(word : String) : Int{
        when (word) {
            "ga" -> return R.raw.ga
            "gi" -> return R.raw.gi
            "gu" -> return R.raw.gu
            "ge" -> return R.raw.ge
            "go" -> return R.raw.go
            "za" -> return R.raw.za
            "zo" -> return R.raw.zo
            "ze" -> return R.raw.ze
            "da" -> return R.raw.da
            "do" -> return R.raw.doo
            "de" -> return R.raw.de
            "ba" -> return R.raw.ba
            "bi" -> return R.raw.bi
            "bu" -> return R.raw.bu
            "be" -> return R.raw.be
            "bo" -> return R.raw.bo
            "pa" -> return R.raw.pa
            "pi" -> return R.raw.pi
            "pu" -> return R.raw.pu
            "pe" -> return R.raw.pe
            "po" -> return R.raw.po
            "kya" -> return R.raw.kya
            "kyu" -> return R.raw.kyu
            "kyo" -> return R.raw.kyo
            "sha" -> return R.raw.sha
            "shu" -> return R.raw.shu
            "sho" -> return R.raw.sho
            "cha" -> return R.raw.cha
            "chu" -> return R.raw.chu
            "cho" -> return R.raw.cho
            "nya" -> return R.raw.nya
            "nyu" -> return R.raw.nyu
            "nyo" -> return R.raw.nyo
            "hya" -> return R.raw.hya
            "hyu" -> return R.raw.hyu
            "hyo" -> return R.raw.hyo
            "mya" -> return R.raw.mya
            "myu" -> return R.raw.myu
            "myo" -> return R.raw.myo
            "rya" -> return R.raw.rya
            "ryu" -> return R.raw.ryu
            "ryo" -> return R.raw.ryo
            "gya" -> return R.raw.gya
            "gyu" -> return R.raw.gyu
            "gyo" -> return R.raw.gyo
            "bya" -> return R.raw.bya
            "byu" -> return R.raw.byu
            "byo" -> return R.raw.byo
            "pya" -> return R.raw.pya
            "pyu" -> return R.raw.pyu
            "pyo" -> return R.raw.pyo
        }
        if (word == "zya" || word == "dya" || word == "dya/ja") {
            return R.raw.ja
        }
        if (word == "zyu" || word == "dyu" || word == "dyu/ju") {
            return R.raw.ju
        }
        if (word == "zyo" || word == "dyo"|| word == "dyo/jo") {
            return R.raw.jo
        }
        if (word == "zi" || word == "di" || word == "zi/ji" || word == "di(zi)") {
            return R.raw.ji
        }
        return if (word == "zu" || word == "du" || word == "du(zu)") {
            R.raw.zu
        } else {
            return -1
        }

    }



    fun sound(jpWord: JPWord,jpVoice: JPVoice,word : String){
        try {
            if (word.isEmpty()) return
            val mediaPlayer = if (jpWord == JPWord.All){
                    MediaPlayer.create(context,returnRomaByRomaAll(word))
                }else{
                    when (jpVoice) {
                        is JPVoice.Clear-> MediaPlayer.create(context,returnRomaByRoma(word))
                        is JPVoice.Foodty , JPVoice.Bend-> MediaPlayer.create(context,returnRomaByFoodyBendRoma(word))
                    }
                }
            if (mediaPlayer.isPlaying) return
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        }catch (e : Resources.NotFoundException){
            return
        }

    }

    private fun returnRomaByRomaAll(word: String) : Int{
        return if (returnRomaByRoma(word) == 0){
            returnRomaByFoodyBendRoma(word)
        }else{
            returnRomaByRoma(word)
        }
    }


    fun soundVocabulary(playResources: String){
        try {
        if (playResources.isEmpty()) return
        val mediaPlayer = MediaPlayer.create(
            context,
            context.resources.getIdentifier(playResources, "raw", context.packageName)
        )
        if (mediaPlayer.isPlaying) return
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        }catch (e : Resources.NotFoundException){
         return
     }
    }



}

