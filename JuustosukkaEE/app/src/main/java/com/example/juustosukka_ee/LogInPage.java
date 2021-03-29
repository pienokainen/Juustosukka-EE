package com.example.juustosukka_ee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInPage extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        button = (Button) findViewById(R.id.logIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    public void logIn(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void openNewUser(View v){
        Intent intent = new Intent(this, NewUserPage.class);
        startActivity(intent);
        
    }
        // Moi tästä alkaa meiän projekti!!

        // JEE

        //juustonakhu


}