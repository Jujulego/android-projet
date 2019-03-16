package net.capellari.julien.projetandroid

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Companion
    companion object {
        const val TAG = "MainActivity"
    }

    // Attributs
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var drawerToggle: ActionBarDrawerToggle

    // Propriétés
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private val isAtTopLevel
        get() = navController.currentDestination?.let { isAtTopLevel(it.id) } ?: true

    // Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    override fun onStart() {
        super.onStart()

        if (isAtTopLevel) {
            drawerToggle.syncState()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (isAtTopLevel && drawerToggle.onOptionsItemSelected(item))
                || onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    // Méthodes
    fun setupNavigation() {
        // Configuration
        appBarConfig = AppBarConfiguration.Builder(nav_view.menu)
                .setDrawerLayout(drawer_layout)
                .build()

        // Setup toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        setupActionBarWithNavController(navController, appBarConfig)

        // Drawer
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.nav_open, R.string.nav_close)
        drawer_layout.addDrawerListener(drawerToggle)

        setupWithNavController(nav_view, navController)

        // Events
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Top level ?
            if (isAtTopLevel(destination.id)) {
                drawerToggle.syncState()

                drawer_layout.closeDrawers()
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    private fun isAtTopLevel(@IdRes id: Int): Boolean {
        return appBarConfig.topLevelDestinations.contains(id)
    }
}