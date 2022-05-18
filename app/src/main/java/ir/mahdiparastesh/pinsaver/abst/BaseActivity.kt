package ir.mahdiparastesh.pinsaver.abst

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.R
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import ir.mahdiparastesh.pinsaver.more.Delay
import ir.mahdiparastesh.pinsaver.view.UiTools.Companion.isReady

abstract class BaseActivity : AppCompatActivity(), OnInitializationCompleteListener {
    val c: Context get() = applicationContext
    val dm: DisplayMetrics by lazy { resources.displayMetrics }
    val dirRtl by lazy { c.resources.getBoolean(R.bool.dirRtl) }
    private var retryForAd = 0

    companion object {
        const val ADMOB_DELAY = 2000L
        const val MAX_AD_RETRY = 2
        var adsInitStatus: InitializationStatus? = null

        fun Context.night(): Boolean = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        fun Context.font(@StringRes fileName: Int): Typeface =
            Typeface.createFromAsset(assets, "fonts/${resources.getString(fileName)}")
    }

    override fun setContentView(root: View?) {
        super.setContentView(root)
        root?.layoutDirection =
            if (!dirRtl) ViewGroup.LAYOUT_DIRECTION_LTR else ViewGroup.LAYOUT_DIRECTION_RTL
    }

    override fun onStart() {
        super.onStart()
        if (adsInitStatus?.isReady() != true)
            Delay(ADMOB_DELAY) { initAdmob() }
        else onInitializationComplete(adsInitStatus!!)
    }

    private fun initAdmob() {
        retryForAd = 0
        MobileAds.initialize(c, this)
    }

    override fun onInitializationComplete(adsInitStatus: InitializationStatus) {
        Companion.adsInitStatus = adsInitStatus
        if (!adsInitStatus.isReady()) {
            if (retryForAd < MAX_AD_RETRY) Delay(ADMOB_DELAY) {
                initAdmob()
                retryForAd++
            } else retryForAd = 0
            return; }
    }
}
