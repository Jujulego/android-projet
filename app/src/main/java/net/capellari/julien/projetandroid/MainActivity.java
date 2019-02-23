package net.capellari.julien.projetandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    // Attributs
    private NavController navController;
    private AppBarConfiguration appBarConfig;

    private ActionBarDrawerToggle drawerToggle;

    // Events
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_main);
        setupNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isAtTopLevel()) {
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (isAtTopLevel() && drawerToggle.onOptionsItemSelected(item))
                || NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    // Méthodes
    void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Vues
        final Toolbar toolbar = findViewById(R.id.toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navView = findViewById(R.id.nav_view);

        // Configuration
        appBarConfig = new AppBarConfiguration.Builder(navView.getMenu())
                .setDrawerLayout(drawer)
                .build();

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);

        // Drawer
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(drawerToggle);

        NavigationUI.setupWithNavController(navView, navController);

        // Events
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // Top level ?
                if (isAtTopLevel(destination.getId())) {
                    drawerToggle.syncState();

                    drawer.closeDrawers();
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        });
    }

    boolean isAtTopLevel() {
        return isAtTopLevel(navController.getCurrentDestination().getId());
    }

    boolean isAtTopLevel(@IdRes int id) {
        return appBarConfig.getTopLevelDestinations().contains(id);
    }
}
