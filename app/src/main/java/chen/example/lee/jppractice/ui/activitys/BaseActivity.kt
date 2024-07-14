package chen.example.lee.jppractice.ui.activitys

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(){

    override fun attachBaseContext(newBase: Context?) {
        val newContext: Context
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            baseContext?.resources?.let {
                val displayMetrics = baseContext.resources.displayMetrics
                val configuration: Configuration = baseContext.resources.configuration
                if (displayMetrics.densityDpi != DisplayMetrics.DENSITY_DEVICE_STABLE) {
                    // Current density is different from Default Density. Override it
                    displayMetrics.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
                    configuration.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE
                    newContext = baseContext //baseContext.createConfigurationContext(configuration);
                } else {
                    // Same density. Just use same context
                    newContext = baseContext
                }
            }

        }

        super.attachBaseContext(newBase)
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            applyOverrideConfiguration(newOverride)
        }
    }
}