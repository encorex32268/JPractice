package chen.example.lee.jppractice.other

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import chen.example.lee.jppractice.db.model.JPContract.SHAREDPREFERENCES_DATA_NAME
import com.example.lee.jppractice.R

/**
 * Activity
 */
fun Activity.getResourceString(key : Int) : String{
   return resources.getString(key)
}
/**
 * Fragment
 */
fun Fragment.getResourcesString(key: Int) : String?{
    return requireContext().resources.getString(key)
}
fun Fragment.getResourcesStringWithVar(key: Int,num : Int) : String{
    return requireContext().resources.getString(key,num)
}

fun Fragment.getResourcesStringArray(key: Int) : Array<String>{
    return requireContext().resources.getStringArray(key)
}

fun Fragment.getVoiceIconByStatus(isOpenVoice : Boolean) : Drawable?{
    return if (isOpenVoice) {
        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_volume_up)
    } else {
        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_volume_off)

    }
}
fun Fragment.addBackButton(){
    setHasOptionsMenu(true)
    (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
}
fun Fragment.removeBackButton(){
    (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
}


