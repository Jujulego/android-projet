package net.capellari.julien.projetandroid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    // Attributs
    private NavController navController;

    // Events
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_main);
        setupNavigation();
    }

    // Méthodes
    void setupNavigation() {
        // Configuration
        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.fragment_main
        ).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Ajout de la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
