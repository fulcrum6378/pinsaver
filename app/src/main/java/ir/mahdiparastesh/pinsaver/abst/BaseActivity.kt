package ir.mahdiparastesh.pinsaver.abst

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    val c: Context get() = applicationContext
}
