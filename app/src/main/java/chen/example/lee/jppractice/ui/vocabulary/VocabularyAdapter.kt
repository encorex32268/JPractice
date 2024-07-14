package chen.example.lee.jppractice.ui.vocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.db.model.Vocabulary
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import chen.example.lee.jppractice.ui.vocabulary.VocabularyViewHolder
import com.example.lee.jppractice.R

class VocabularyAdapter(
    var vocabularys: List<Vocabulary>,
    private val soundHandler: SoundHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository
)
    : RecyclerView.Adapter<VocabularyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val view  = LayoutInflater.from(parent.context)
            .inflate(R.layout.vocabulary_item,parent,false)
        return VocabularyViewHolder(view, soundHandler,sharedPreferencesRepository)
    }
    override fun getItemCount(): Int {
        return vocabularys.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        holder.bindTo(vocabularys[position])
        holder.itemView.tag = position
    }

}