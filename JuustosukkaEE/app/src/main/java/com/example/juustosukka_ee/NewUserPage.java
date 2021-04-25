package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.cloud.datastore.core.number.NumberParts;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class NewUserPage extends AppCompatActivity {
    private ImageButton returnButton;
    Button createAccount;
    EditText email, passwrd, password2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuserpage);
        email = findViewById(R.id.username_input);
        passwrd = findViewById(R.id.passwd);
        password2 = findViewById(R.id.passwd_again);
        mAuth = FirebaseAuth.getInstance();
        returnButton = (ImageButton) findViewById(R.id.returnbutton);
        createAccount = (Button) findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   System.out.println("Password error1");
                if (testPassword(passwrd.getText().toString(),password2.getText().toString())) {
                    createAccount(email.getText().toString(), passwrd.getText().toString());
                }else{
                    System.out.println("Password error");
                }
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openLoginPage();
            }
        });



    }



    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        String hash = PasswordHash.getPassWordHash(password,email);
        mAuth.createUserWithEmailAndPassword(email, hash)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(NewUserPage.this, "Käyttäjän luominen onnistui",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            openLoginPage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(NewUserPage.this, "Käyttäjän luominen epäonnistui.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }
    public void openLoginPage(){
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }


    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
    //Kirjautumisen 	salasana noudattaa hyvän salasanan sääntöjä (sisältää vähintään yhden numeron, erikoismerkin, ison ja pienen kirjaimen, on vähintään 12 merkkiä pitkä)
    private boolean testPassword(String password, String password_again) {
        System.out.println("Password error");
        if (!password.matches(".*\\d.*")){
            Toast.makeText(NewUserPage.this, "Salasana tulee sisältää vähintään yhden numeron.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!containsUpperCaseLetter(password)){
            Toast.makeText(NewUserPage.this, "Salasana tulee sisältää vähintään yhden ison kirjaimen.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!containsLowerCaseLetter(password)){
            Toast.makeText(NewUserPage.this, "Salasana tulee sisältää vähintään yhden pienen kirjaimen.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!special(password)){
            Toast.makeText(NewUserPage.this, "Salasana tulee sisältää vähintään yhden erikoismerkin.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        System.out.println("Password error");
        if (password.length() < 12){
            Toast.makeText(NewUserPage.this, "Salasanassa tulee olla vähintään 12 merkkiä.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(password_again)){
            Toast.makeText(NewUserPage.this, "Salasanat eivät täsmää.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean containsUpperCaseLetter(String s){
        for(int i=0;i<s.length();i++){
            if(Character.isUpperCase(s.charAt(i))){
                return true;
            }
        }
        return false;
    }

    public boolean containsLowerCaseLetter(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean special(String s){
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(specialChars.contains(Character.toString(c))){
                return true;
            }
        }
        return false;
    }
}