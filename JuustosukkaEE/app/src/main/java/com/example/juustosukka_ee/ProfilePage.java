package com.example.juustosukka_ee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {
    TextView textField;
    EditText editText;
    Button log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        log_out = (Button) findViewById(R.id.logout_button);

        /*EditText editWeight = findViewById(R.id.start_weight);
        EditText editHeight = findViewById(R.id.height);

        TextView editWeight = findViewById(R.id.height);*/

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        return true;
                    case R.id.data:
                        startActivity(new Intent(getApplicationContext(), DataPage.class));
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


    public void log_out(View v){
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }


}
