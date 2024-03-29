package ir.mahdiparastesh.pinsaver.more

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock

open class Delay(
    private val timeout: Long = 5000L,
    private val looper: Looper = Looper.myLooper()!!,
    private val listener: () -> Unit,
) {
    private var mStopTimeInFuture = SystemClock.elapsedRealtime() + timeout
    private val mHandler = object : Handler(looper) {
        override fun handleMessage(msg: Message) {
            synchronized(this@Delay) {
                if (mStopTimeInFuture - SystemClock.elapsedRealtime() <= 0)
                    listener()
                else sendMessageDelayed(obtainMessage(MSG), timeout)
            }
        }
    }

    init {
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
    }

    companion object {
        private const val MSG = 1
    }
}
