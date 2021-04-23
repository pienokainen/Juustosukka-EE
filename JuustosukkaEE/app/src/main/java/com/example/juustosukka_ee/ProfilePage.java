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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import static android.content.ContentValues.TAG;

public class ProfilePage extends AppCompatActivity {
    TextView textField, username, aget, heightt, weightt, htownt;
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
        aget = findViewById(R.id.ikatext);
        heightt = findViewById(R.id.pituustext);
        weightt = findViewById(R.id.painotext);
        htownt = findViewById(R.id.kaupunkitext);
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
        setValues();
    }



    public void log_out(View v){
        mAuth.signOut();
        Intent intent = new Intent(this, LogInPage.class);
        startActivity(intent);
    }
    public void save_info(View v){
        String age = agefield.getText().toString();
        if (age != null){setUserAge(age);
            aget.setText("Ikä");}

        String weight = weight_pp.getText().toString();
        if (weight != null){setUserWeight(weight);
            weightt.setText("Paino (kg)");}

        String height = height_pp.getText().toString();
        if (height != null){setUserHeight(height);
            heightt.setText("Pituus (cm)");}

        String hometown = hometown_pp.getText().toString();
        if (hometown != null){setUserHometown(hometown);
            htownt.setText("Kotikaupunki");}

        Log.d(TAG, "UserInformationUpdate:success"+ "   AGE: "+age+ "   WEIGHT: "+weight +"   HEIGHT: "+height+ "   HOMETOWN: "+hometown);


        Log.d(TAG, "UserInformationUpdate:success"+ "   AGE: "+age+ "   WEIGHT: "+weight +"   HEIGHT: "+height+ "   HOMETOWN: "+hometown);

    }

    public void setUserAge(String age) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(currentUserID).child("ika").setValue(age);
    }

    public void setUserWeight(String weight) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        double weightd = Double.parseDouble(weight);
        mDatabase.child("Users").child(currentUserID).child("aloituspaino").setValue(weightd);
    }

    public void setUserHeight(String height) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        double heightd = Double.parseDouble(height);
        mDatabase.child("Users").child(currentUserID).child("pituus").setValue(heightd);
    }


    public void setUserHometown(String hometown) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(currentUserID).child("kotikaupunki").setValue(hometown);
    }


    public void setValues(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(currentUserID).child("ika").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null ) {
                        String age = (String.valueOf(task.getResult().getValue()));
                        agefield.setText(age);
                        aget.setText("Ikä");
                    }
                }
            }
        });

        mDatabase.child("Users").child(currentUserID).child("aloituspaino").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null ){
                        String paino = (String.valueOf(task.getResult().getValue()));
                        weight_pp.setText(paino);
                        weightt.setText("Paino (kg)");
                    }
                }
            }
        });

        mDatabase.child("Users").child(currentUserID).child("pituus").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null ) {
                        String pituus = (String.valueOf(task.getResult().getValue()));
                        height_pp.setText(pituus);
                        heightt.setText("Pituus (cm)");
                    }
                }
            }
        });
        mDatabase.child("Users").child(currentUserID).child("kotikaupunki").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().getValue() != null ) {
                        String kotikaupunki = (String.valueOf(task.getResult().getValue()));
                        hometown_pp.setText(kotikaupunki);
                        htownt.setText("Kotikaupunki");
                    }
                }
            }
        });
    }




}
