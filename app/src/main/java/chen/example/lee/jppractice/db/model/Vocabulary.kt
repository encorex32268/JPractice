package chen.example.lee.jppractice.db.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Vocabulary(
    val hiragana : String,
    val word : String,
    val wordExplain:String,
    val type : String,
    val example : String,
    val exampleHiragana : String,
    val exampleTranslate : String,
    val wordPlayResourceName :String,
    val examplePlayResourceName : String,
    val wrongWord : String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    var isFliped  = false

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
        id = parcel.readInt()
        isFliped = parcel.readByte() != 0.toByte()
    }

    override fun toString(): String {
        return "Vocabulary(hiragana='$hiragana', word='$word', wordExplain='$wordExplain', type='$type', example='$example', exampleHiragana='$exampleHiragana', exampleTranslate='$exampleTranslate', wordPlayResourceName='$wordPlayResourceName', examplePlayResourceName='$examplePlayResourceName', id=$id, isFliped=$isFliped , 'wrongWord=$wrongWord')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hiragana)
        parcel.writeString(word)
        parcel.writeString(wordExplain)
        parcel.writeString(type)
        parcel.writeString(example)
        parcel.writeString(exampleHiragana)
        parcel.writeString(exampleTranslate)
        parcel.writeString(wordPlayResourceName)
        parcel.writeString(examplePlayResourceName)
        parcel.writeInt(id)
        parcel.writeByte(if (isFliped) 1 else 0)
        parcel.writeString(wrongWord)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vocabulary> {
        override fun createFromParcel(parcel: Parcel): Vocabulary {
            return Vocabulary(parcel)
        }

        override fun newArray(size: Int): Array<Vocabulary?> {
            return arrayOfNulls(size)
        }
    }


}