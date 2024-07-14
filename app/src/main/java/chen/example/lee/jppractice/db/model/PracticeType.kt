package chen.example.lee.jppractice.db.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PracticeType(
    val saveWord : JPWord ,
    val saveToneInt: JPVoice,
    val saveCount:Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(JPWord::class.java.classLoader)!!,
        parcel.readParcelable(JPVoice::class.java.classLoader)!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(saveWord, flags)
        parcel.writeParcelable(saveToneInt, flags)
        parcel.writeInt(saveCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "PracticeType(saveWord=$saveWord, saveToneInt=$saveToneInt, saveCount=$saveCount)"
    }

    companion object CREATOR : Parcelable.Creator<PracticeType> {
        override fun createFromParcel(parcel: Parcel): PracticeType {
            return PracticeType(parcel)
        }

        override fun newArray(size: Int): Array<PracticeType?> {
            return arrayOfNulls(size)
        }
    }




}


sealed class JPVoice : Parcelable {
    @Parcelize
    object Clear : JPVoice()
    @Parcelize
    object Foodty : JPVoice()
    @Parcelize
    object Bend : JPVoice()
}

sealed class JPWord : Parcelable{
    @Parcelize
    object All : JPWord()
    @Parcelize
    object Hiragana : JPWord()
    @Parcelize
    object Katakana : JPWord()
}



