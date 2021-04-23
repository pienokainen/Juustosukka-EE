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
                    showToast(v, "Salasanassa tulee olla vähintään 12 merkkiä.");

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
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            openLoginPage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
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

    public void showToast (View v,CharSequence text) {
        Context context = getApplicationContext();
        //CharSequence text = "Käyttäjätili luotu!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
    private boolean testPassword(String password, String password_again) {
        System.out.println("Password errorppppppppppppppppppppppppppppppppppppppp");
        boolean compliant = false;
        if (password.length() > 11) {
            compliant = true;
        }
        if (password.equals(password2)){
            compliant = true;
        }
        return compliant;
    }
}