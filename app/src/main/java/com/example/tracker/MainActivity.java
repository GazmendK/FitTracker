package com.example.tracker;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tracker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    //private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Konfiguration der Top-Level-Destinationen
//        appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
//                R.id.navigation_notifications
//        ).build();

        // Navigation Controller und Toolbar mit NavController verknüpfen
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);




        // BottomNavigationView konfigurieren
        navView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Deaktiviert den Zurück-Pfeil
//        getSupportActionBar().setHomeButtonEnabled(false);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int destinationId = 0;

                if (item.getItemId() == R.id.navigation_home) {
                    destinationId = R.id.FirstFragment;
                } else if (item.getItemId() == R.id.navigation_dashboard) {
                    destinationId = R.id.SecondFragment;
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    destinationId = R.id.ThirdFragment;
                } else {
                    return false;
                }

                // Navigation mit Animationen ausführen
                navController.navigate(destinationId, null, getNavOptionsWithAnimations());
                return true;
            }
        });
    }




    private NavOptions getNavOptionsWithAnimations() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)    // Animation für eintretendes Fragment
                .setExitAnim(R.anim.fade_out)   // Animation für austretendes Fragment
                .setPopEnterAnim(R.anim.slide_in_left)  // Animation beim Zurückspringen
                .setPopExitAnim(R.anim.slide_out_right) // Animation beim Verlassen
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu); // bin mir hier nicht sicher
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutUs) {
            // Zeige das About Us-Fragment oder eine DialogBox
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.include ,new AboutUsFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // Zum vorherigen Fragment navigieren
        } else {
            super.onBackPressed(); // App beenden
        }
    }

    private void showAboutUsDialog() {
        // Dialog mit Informationen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Us")
                .setMessage("R.string.licenses")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}