package com.example.compraagro;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.R.menu.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.compraagro.fragments.HistoryFragment;
import com.example.compraagro.fragments.MapFragment;
import com.example.compraagro.fragments.MarketFragment;
import com.example.compraagro.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    MarketFragment marketFragment;
    MapFragment mapFragment;
    HistoryFragment historyFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marketFragment = new MarketFragment();
        mapFragment = new MapFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();

        ImageButton btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext() ,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(getApplicationContext(),"Sesion cerrada",Toast.LENGTH_LONG).show();
            }
        });



        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.nav_market:
                    loadFragment(marketFragment);
                    return true;
                case R.id.nav_map:
                    loadFragment(mapFragment);
                    return true;
                case R.id.nav_history:
                    loadFragment(historyFragment);
                    return true;
                case R.id.nav_profile:
                    loadFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentMenu,fragment).addToBackStack(null);
        transaction.commit();
    }

};
