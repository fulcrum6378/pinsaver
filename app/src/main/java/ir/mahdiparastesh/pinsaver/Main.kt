package ir.mahdiparastesh.pinsaver

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import ir.mahdiparastesh.pinsaver.abst.BaseActivity
import ir.mahdiparastesh.pinsaver.databinding.MainBinding
import ir.mahdiparastesh.pinsaver.more.Delay

class Main : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var b: MainBinding
    private lateinit var toggleNav: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MainBinding.inflate(layoutInflater)
        setContentView(b.root)
        initToolbar(b.toolbar, R.string.app_name)

        // Navigation
        toggleNav = ActionBarDrawerToggle(
            this, b.root, b.toolbar, R.string.open, R.string.close
        ).apply {
            b.root.addDrawerListener(this)
            isDrawerIndicatorEnabled = true
            if (!night()) drawerArrowDrawable =
                drawerArrowDrawable.apply { colorFilter = themePdcf() }
            syncState()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private var exiting = false
    override fun onBackPressed() {
        if (b.root.isDrawerOpen(GravityCompat.START)) {
            b.root.closeDrawer(GravityCompat.START)
            toggleNav.syncState()
            return; }
        if (!exiting) {
            exiting = true
            Delay(4000L) { exiting = false }
            Toast.makeText(c, R.string.toExit, Toast.LENGTH_SHORT).show()
            return; }
        super.onBackPressed() // Do NOT kill the process
    }
}
