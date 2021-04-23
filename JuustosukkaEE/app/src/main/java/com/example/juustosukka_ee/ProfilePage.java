package com.example.juustosukka_ee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    TextView textField, username;
    EditText editText, password,agefield, weight_pp, height_pp,hometown_pp;
    Button log_out, save;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        log_out = (Button) findViewById(R.id.logout_button);
        username = findViewById(R.id.username_profilepage);
        agefield = findViewById(R.id.age_pp);
        save = findViewById(R.id.save_button);
        weight_pp = findViewById(R.id.start_weight);
        height_pp = findViewById(R.id.height);
        hometown_pp = findViewById(R.id.home_town);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }
        /*EditText editWeight = findViewById(R.id.start_weight);
        EditText editHeight = findViewById(R.id.height);

        TextView editWeight = findViewById(R.id.height);*/
        System.out.println(mAuth.getCurrentUser().getEmail());
        username.setText(mAuth.getCurrentUser().getEmail());
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
        mAuth.signOut();
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }
    public void save_info(View v){
        String age = agefield.getText().toString();
        String weight = weight_pp.getText().toString();
        String height = height_pp.getText().toString();
        setUserHeight(height);
        String hometown = hometown_pp.getText().toString();
        Log.d(TAG, "UserInformationUpdate:success"+ "   AGE: "+age+ "   WEIGHT: "+weight +"   HEIGHT: "+height+ "   HOMETOWN: "+hometown);


        Log.d(TAG, "UserInformationUpdate:success"+ "   AGE: "+age+ "   WEIGHT: "+weight +"   HEIGHT: "+height+ "   HOMETOWN: "+hometown);

    }

    public void setUserHeight(String height) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(currentUserID).child("pituus").setValue(height);
    }


}
