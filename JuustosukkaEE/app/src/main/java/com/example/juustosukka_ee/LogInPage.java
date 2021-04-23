package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LogInPage extends AppCompatActivity {
    private Button loginbutton;
    EditText email, passwrd;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        email = findViewById(R.id.username_input);
        passwrd = findViewById(R.id.passwd);

        mAuth = FirebaseAuth.getInstance();
        loginbutton = (Button) findViewById(R.id.logIn);

        loginbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn(email.getText().toString(), passwrd.getText().toString());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        String hash = PasswordHash.getPassWordHash(password,email);
        //System.out.println("#########################"+hash);
        mAuth.signInWithEmailAndPassword(email, hash)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            logIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInPage.this, "Käyttäjänimi tai salasana väärin",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    public void logIn(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    public void homepagelle(View v){
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

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}