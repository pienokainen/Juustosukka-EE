package com.example.juustosukka_ee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewUser();
            }
        });
    }

    public void openNewUser(){
        Intent intent = new Intent(this, NewUser.class);
        startActivity(intent);
    }

        // Moi tästä alkaa meiän projekti!!

        // JEE

        //juustonakhu


}