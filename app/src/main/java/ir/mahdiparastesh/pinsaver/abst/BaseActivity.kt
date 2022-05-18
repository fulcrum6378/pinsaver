package ir.mahdiparastesh.pinsaver.abst

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import ir.mahdiparastesh.pinsaver.Main
import ir.mahdiparastesh.pinsaver.R
import ir.mahdiparastesh.pinsaver.more.Delay
import ir.mahdiparastesh.pinsaver.view.UiTools.Companion.isReady
import ir.mahdiparastesh.pinsaver.view.UiTools.Companion.themeColor

abstract class BaseActivity : AppCompatActivity(), OnInitializationCompleteListener {
    val c: Context get() = applicationContext
    val dm: DisplayMetrics by lazy { resources.displayMetrics }
    private val dirRtl by lazy { c.resources.getBoolean(R.bool.dirRtl) }
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

    var tbTitle: TextView? = null
    fun initToolbar(
        tb: Toolbar, title: Int, /*font: Typeface = fontBold,*/ changeTitleTo: String? = null
    ) {
        for (g in 0 until tb.childCount) {
            val getTitle = tb.getChildAt(g)
            if (getTitle is TextView && getTitle.text.toString() == getString(title))
                tbTitle = getTitle
        }
        if (changeTitleTo != null) tbTitle?.text = changeTitleTo
        //tbTitle?.typeface =
        if (this !is Main) tb.setNavigationOnClickListener { onBackPressed() }
    }

    fun pdcf(@ColorRes res: Int) =
        PorterDuffColorFilter(ContextCompat.getColor(this, res), PorterDuff.Mode.SRC_IN)

    fun themePdcf(@AttrRes attr: Int = android.R.attr.colorPrimaryDark) =
        PorterDuffColorFilter(themeColor(attr), PorterDuff.Mode.SRC_IN)
}
