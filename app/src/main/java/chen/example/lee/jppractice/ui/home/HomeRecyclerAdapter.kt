package chen.example.lee.jppractice.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import com.example.lee.jppractice.R

class HomeRecyclerAdapter(
    var allWordToneData: AllWordToneData,
    private val soundHandler: SoundHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : RecyclerView.Adapter<HomeViewHolder>(){
    private var isReverse = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alltone_customview,parent,false)
        return  HomeViewHolder(view,soundHandler,sharedPreferencesRepository)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = allWordToneData.romaDataList.size


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindTo(allWordToneData,position,isReverse)
    }
    fun setAllReverse(isReverse: Boolean) {
        this.isReverse = isReverse
    }


}