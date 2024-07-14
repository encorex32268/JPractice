package chen.example.lee.jppractice.ui.activitys


import android.os.Bundle
import android.view.View
import androidx.navigation.ui.setupWithNavController
import chen.example.lee.jppractice.*
import chen.example.lee.jppractice.db.room.VocabularyDao
import chen.example.lee.jppractice.other.getResourceString
import com.github.javiersantos.appupdater.AppUpdater
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.ActivityMainBinding
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity :  BaseActivity() {

    private lateinit var mainBinding : ActivityMainBinding
    @Inject lateinit var vocabularyDao : VocabularyDao
    @Inject lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        getGooglePlayVersion()
        setUpBottomNavigation()
        scope.launch {
            vocabularyDao.getAll()
        }

    }

    private fun setUpBottomNavigation() {
        mainBinding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
            bottomNavigation.apply {
                setOnNavigationItemReselectedListener {}
                setupWithNavController(navHostFragment!!.findNavController())
                navHostFragment.findNavController()
                    .addOnDestinationChangedListener { controller, destination, arguments ->
                        when (destination.id) {
                            R.id.homeFragment, R.id.examSettingFragment, R.id.vocabularyFragment -> {
                                bottomNavigation.visibility = View.VISIBLE
                            }
                            else -> bottomNavigation.visibility = View.GONE
                        }
                    }
            }
        }
    }
    private fun getGooglePlayVersion() {
        AppUpdater(this@MainActivity).apply {
            setTitleOnUpdateAvailable(getResourceString(R.string.main_activity_title))
            setContentOnUpdateAvailable(getResourceString(R.string.main_activity_version))
            setButtonUpdate(getResourceString(R.string.main_activity_update_button_text))
            setButtonDismiss(getResourceString(R.string.main_activity_cancel_button_text))
            setButtonDoNotShowAgain(getResourceString(R.string.main_activity_nevershow_button_text))
        }.start()

    }



}
