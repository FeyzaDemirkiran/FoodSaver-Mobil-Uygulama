package com.example.foodsaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;

    protected void setupNavigationDrawer(@LayoutRes int layoutResId) {
        setContentView(layoutResId);


        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigation(item);
            return true;
        });

        setupBottomNav();
    }

    protected void setupBottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.anasayfaikonn) {
                startActivity(new Intent(this, AnasayfaActivity.class));
                return true;
            } else if (id == R.id.eklemeikonn) {
                startActivity(new Intent(this, YeniUrunEkleActivity.class));
                return true;
            } else if (id == R.id.favoriikonn) {
                startActivity(new Intent(this, FavoriTariflerActivity.class));
                return true;
            } else if (id == R.id.hesabimikonn) {
                startActivity(new Intent(this, HesabimActivity.class));
                return true;
            }

            return false;
        });
    }

    private void handleNavigation(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, AnasayfaActivity.class));
        } else if (id == R.id.nav_favoriler) {
            startActivity(new Intent(this, FavoriTariflerActivity.class));
        } else if (id == R.id.nav_muhafaza) {
            startActivity(new Intent(this, CategoryActivity.class));
        } else if (id == R.id.nav_urunlerim) {
            startActivity(new Intent(this, UrunlerimActivity.class));
        }  else if (id == R.id.nav_tarif) {
            startActivity(new Intent(this, TariflerActivity.class));
        }


        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

}