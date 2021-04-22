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
<<<<<<< HEAD
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfilePage extends AppCompatActivity {
    TextView textField;
    EditText height;
    Button log_out;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
=======

import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    TextView textField, username;
    EditText editText, password,agefield, weight_pp, height_pp,hometown_pp;
    Button log_out, save;
    private FirebaseAuth mAuth;

>>>>>>> 47691ba50c57166f9fc0621f7241636d66b1f855
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        height = findViewById(R.id.height);
        log_out = (Button) findViewById(R.id.logout_button);
        username = findViewById(R.id.username_profilepage);
        agefield = findViewById(R.id.age_pp);
        save = findViewById(R.id.save_button);
        weight_pp = findViewById(R.id.start_weight);
        height_pp = findViewById(R.id.height);
        hometown_pp = findViewById(R.id.home_town);
        /*EditText editWeight = findViewById(R.id.start_weight);
        EditText editHeight = findViewById(R.id.height);

        TextView editWeight = findViewById(R.id.height);*/
        mAuth = FirebaseAuth.getInstance();
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

<<<<<<< HEAD
    public void setHeight(View v){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String userid = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");
        reference.child("Pituus").setValue(height.getText().toString());

    }

=======
>>>>>>> 47691ba50c57166f9fc0621f7241636d66b1f855
    public void log_out(View v){
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }
    public void save_info(View v){
        String age = agefield.getText().toString();
        String weight = weight_pp.getText().toString();
        String height = height_pp.getText().toString();
        String hometown = hometown_pp.getText().toString();

        Log.d(TAG, "UserInformationUpdate:success"+ "   AGE: "+age+ "   WEIGHT: "+weight +"   HEIGHT: "+height+ "   HOMETOWN: "+hometown);

    }

}
