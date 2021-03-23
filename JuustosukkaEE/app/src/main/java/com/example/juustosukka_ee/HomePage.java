package com.example.juustosukka_ee;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomePage extends AppCompatActivity {
    private ImageButton databutton;
    private ImageButton homebutton;
    private ImageButton profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        databutton = (ImageButton) findViewById(R.id.databutton);
        databutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDataPage();
            }
        });

        homebutton = (ImageButton) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        profilebutton = (ImageButton) findViewById(R.id.profilebutton);
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });
    }

    public void openProfilePage(){
        Intent intent1 = new Intent(this, ProfilePage.class);
        startActivity(intent1);
    }

    public void openDataPage(){
        Intent intent2 = new Intent(this, DataPage.class);
        startActivity(intent2);
    }

    public void openHomePage(){
        Intent intent3 = new Intent(this, HomePage.class);
        startActivity(intent3);
    }

}