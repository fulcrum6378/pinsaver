package ir.mahdiparastesh.pinsaver

import android.os.Bundle
import ir.mahdiparastesh.pinsaver.abst.BaseActivity
import ir.mahdiparastesh.pinsaver.databinding.MainBinding

class Main : BaseActivity() {
    private lateinit var b: MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MainBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}
