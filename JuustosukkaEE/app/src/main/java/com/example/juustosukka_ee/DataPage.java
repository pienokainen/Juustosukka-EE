package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DataPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datapage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);

        bottomNavigationView.setSelectedItemId(R.id.data);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.data:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}