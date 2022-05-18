package ir.mahdiparastesh.pinsaver.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.initialization.InitializationStatus
import ir.mahdiparastesh.pinsaver.R
import ir.mahdiparastesh.pinsaver.abst.BaseActivity

class UiTools {
    companion object {
        private const val ADMOB = "com.google.android.gms.ads.MobileAds"

        fun View.vis(bb: Boolean = true) {
            visibility = if (bb) View.VISIBLE else View.GONE
        }

        fun AlertDialog.stylise(c: BaseActivity) {}

        fun adaptiveBanner(c: BaseActivity, @StringRes unitId: Int) = AdView(c).apply {
            id = R.id.adBanner
            adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                c, (c.dm.widthPixels / c.dm.density).toInt()
            )
            adUnitId = c.getString(unitId)
        }

        fun adaptiveBannerLp() = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID }

        fun InitializationStatus.isReady(): Boolean = if (adapterStatusMap.containsKey(ADMOB))
            adapterStatusMap[ADMOB]?.initializationState == AdapterStatus.State.READY
        else false
    }
}
