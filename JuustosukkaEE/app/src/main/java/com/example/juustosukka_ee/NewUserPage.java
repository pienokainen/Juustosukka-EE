package com.example.juustosukka_ee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewUserPage extends AppCompatActivity {
    private ImageButton returnButton;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuserpage);

        returnButton = (ImageButton) findViewById(R.id.returnbutton);
        createAccount = (Button) findViewById(R.id.createAccount);
    }

    public void openLoginPage(View v){
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }

    public void showToast (View v) {
        Context context = getApplicationContext();
        CharSequence text = "Käyttäjätili luotu!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}