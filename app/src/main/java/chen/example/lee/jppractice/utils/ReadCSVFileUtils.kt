package chen.example.lee.jppractice.utils

import android.content.Context
import android.util.Log
import chen.example.lee.jppractice.db.model.Vocabulary
import com.example.lee.jppractice.R
import com.moji4j.MojiConverter

class ReadCSVFileUtils() {
        private val specialExample = mapOf<String,String>(
            Pair("kabanga古kunaru","kabangafurukunaru"),
            Pair("taihen'omoshiroi","taihenomoshiroi"),
            Pair("「Ｇood」nihongodenantoiu","goodnihongodenantoiu"),
            Pair("nattouga嫌idesu","nattougakiraidesu")
        )
        private val csvFiles = arrayListOf(
            R.raw.doushi,
            R.raw.fukushi,
            R.raw.ikeiyoushi,
            R.raw.meishi,
            R.raw.nakeiyoushi
        )
        fun parseCSV(context: Context) : ArrayList<Vocabulary>{
            val result = arrayListOf<Vocabulary>()
            val convert = MojiConverter()
            val mResources = context.resources
            csvFiles.forEach {
                val openRawResource = mResources.openRawResource(it)
                val csvFireName = mResources.getResourceName(it).replace("raw/","")
                val bufferReader = openRawResource.bufferedReader()
                var line = bufferReader.readLine()
                var type = line
                type = type.replace(",","")
                line = bufferReader.readLine()
                while (line != null){
                    val splits = line.split(",")
                    var wordPlayResourceName = ""
                    var examplePlayResourceName = ""
                    var wrongWord = ""
                    wordPlayResourceName = if(splits[0].isNotEmpty()){
                        var splits0Roma  = convert.convertKanaToRomaji(splits[0])
                        splits0Roma = splits0Roma.replace("/","_")
                        "${csvFireName}_${splits0Roma}"
                    }else{ "" }

                    if (splits[4].isNotEmpty()) {
                        var splits4Roma = convert.convertKanaToRomaji(splits[4])
                        if (specialExample.containsKey(splits4Roma)){
                            splits4Roma = specialExample[splits4Roma]

                        }
                        examplePlayResourceName = "${csvFireName}_${splits4Roma}"
                    }else{
                        val splits3Roma = convert.convertKanaToRomaji(splits[3])
                        examplePlayResourceName = "${csvFireName}_${splits3Roma}"
                    }
                        result.add(
                        Vocabulary(
                            hiragana = splits[0],
                            word = splits[1],
                            wordExplain = splits[2],
                            type = type,
                            example = splits[3],
                            exampleHiragana = splits[4],
                            exampleTranslate = splits[5],
                            wordPlayResourceName = wordPlayResourceName,
                            examplePlayResourceName = examplePlayResourceName,
                            wrongWord = splits[6]
                        )

                    )
                    line = bufferReader.readLine()
                }
            }



            return result
        }


}

