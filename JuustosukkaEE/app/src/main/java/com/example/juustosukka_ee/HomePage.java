package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static androidx.browser.browseractions.BrowserActionsIntent.KEY_TITLE;
import static androidx.browser.customtabs.CustomTabsIntent.KEY_DESCRIPTION;


public class HomePage extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView bmitext;
    EditText weightentry;
    EditText stepsentry;
    FirebaseFirestore db;
    DocumentReference documentReference;

    private FirebaseAuth mAuth;
    LineChart mpstepschart;
    LineChart mpweightchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setProfile();
        mAuth = FirebaseAuth.getInstance();
        weightentry = findViewById(R.id.weight);
        bmitext = findViewById(R.id.bmitextv);
        stepsentry = findViewById(R.id.steps);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.data:
                        startActivity(new Intent(getApplicationContext(), DataPage.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });



        mpweightchart = findViewById(R.id.weightchart);
        LineDataSet lineDataSet1 = new LineDataSet(weightData(), "Weight");
        ArrayList<ILineDataSet> weightdata = new ArrayList<>();
        weightdata.add(lineDataSet1);
        LineData data1 = new LineData(weightdata);
        mpweightchart.setData(data1);
        mpweightchart.invalidate();
        mpweightchart.setDrawBorders(true);
        mpweightchart.setBorderColor(Color.BLACK);
        mpweightchart.setBorderWidth(1);

        //mpweightchart.setBackgroundColor(Color.CYAN);
        XAxis xAxis1 = mpweightchart.getXAxis();
        YAxis yAxisr1 = mpweightchart.getAxisRight();
        YAxis yAxisl1 = mpweightchart.getAxisLeft();

        mpstepschart = findViewById(R.id.stepschart);
        LineDataSet lineDataSet2 = new LineDataSet(stepsData(), "Steps");
        ArrayList<ILineDataSet> stepsdata = new ArrayList<>();
        stepsdata.add(lineDataSet2);
        LineData data2 = new LineData(stepsdata);
        mpstepschart.setData(data2);
        mpstepschart.invalidate();
        mpstepschart.setDrawBorders(true);
        mpstepschart.setBorderColor(Color.BLACK);
        mpstepschart.setBorderWidth(1);
        //mpstepschart.setBackgroundColor(Color.BLACK);
        XAxis xAxis2 = mpstepschart.getXAxis();
        xAxis1.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public String getFormattedValue(float value) {
                LocalDate date1 = LocalDate.ofEpochDay((long) value);
                return date1.format(DateTimeFormatter.ofPattern("dd.MM."));
            }


        });

        yAxisl1.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                return value + "kg";
            }


        });

        yAxisr1.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                return value + "kg";
            }


        });

        xAxis2.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public String getFormattedValue(float value) {
                LocalDate date1 = LocalDate.ofEpochDay((long) value);
                return date1.format(DateTimeFormatter.ofPattern("dd.MM."));
            }


        });
    }

    private ArrayList<Entry> stepsData(){
        ArrayList<Entry> stepslist = new ArrayList<Entry>();
        List<Entry> stepsEntry = getSteps();
        stepslist.add(new Entry(0,0));
        for (Entry c : stepsEntry){
            float x = c.getX();
            float y = c.getY();

            stepslist.add(c);
        }
        stepslist.remove(0);
        return stepslist;
    }
    private ArrayList<Entry> weightData(){
        ArrayList<Entry> weighlist = new ArrayList<Entry>();
        List<Entry> weightEntry = getWeight();
        weighlist.add(new Entry(0,0));
        for (Entry c : weightlist.getInstance().getPaino()){
            float x = c.getX();
            float y = c.getY();

            weighlist.add(c);
        }
        weighlist.remove(0);
        return weighlist;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setWeightentry(View v){
        LocalDate localDate = LocalDate.now();
        String date = String.valueOf(localDate.toEpochDay());
        String userid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> nestedData = new HashMap<>();

        int weightI = Integer.parseInt(weightentry.getText().toString());
        nestedData.put(date, weightI);
        docData.put("Paino", nestedData);
        db.collection("users").document(userid)
                .set(docData, SetOptions.merge());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStepsentry(View v) {
        LocalDate localDate = LocalDate.now();
        String date = String.valueOf(localDate.toEpochDay());
        String userid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> docData2 = new HashMap<>();
        Map<String, Object> nestedData2 = new HashMap<>();

        int stepsI = Integer.parseInt(stepsentry.getText().toString());
        nestedData2.put(date, stepsI);
        docData2.put("Askeleet", nestedData2);


        db.collection("users").document(userid)
                .set(docData2, SetOptions.merge());
    }


    private ArrayList<Entry> getSteps(){

        String userid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        FirebaseFirestore.getInstance().collection("users")
                .document(userid).get()
                .addOnCompleteListener(new
                                               OnCompleteListener<DocumentSnapshot>() {
                                                   @RequiresApi(api = Build.VERSION_CODES.O)
                                                   @Override
                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                       DocumentSnapshot document = task.getResult();

                                                       Map<String, Object> map = document.getData();
                                                       for (Map.Entry<String, Object> entry : map.entrySet()) {
                                                           if (entry.getKey().equals("Askeleet")) {
                                                               Log.d("TAG", entry.getValue().toString());
                                                               System.out.println(entry.getValue().toString());
                                                               List<String> list = new ArrayList<String>();
                                                               list = Arrays.asList(entry.getValue().toString().split(", "));

                                                               for(String s: list){
                                                                   if (s.contains("{")){
                                                                       s = s.replace("{", "");
                                                                   }
                                                                   if(s.contains("}")){
                                                                       s = s.replace("}", "");
                                                                   }
                                                                   String string = s;
                                                                   String[] parts = string.split("=");
                                                                   //int date = Integer.parseInt(parts[0]);
                                                                   String date = parts[0];
                                                                   float weight = Float.parseFloat(parts[1]);

                                                                   float date4 = Float.parseFloat(date);
                                                                   weightlist.getInstance().addsteps(date4,weight);
                                                               }
                                                           }
                                                       }

                                                   }
                                               });
        return weightlist.getInstance().getSteps();
    }


    public void refresh(View v){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    private ArrayList<Entry> getWeight(){

        String userid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        FirebaseFirestore.getInstance().collection("users")
            .document(userid).get()
            .addOnCompleteListener(new
                OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        Map<String, Object> map = document.getData();
                        try{
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                if (entry.getKey().equals("Paino")) {
                                    Log.d("TAG", entry.getValue().toString());
                                    System.out.println(entry.getValue().toString());
                                    List<String> list = new ArrayList<String>();
                                    list = Arrays.asList(entry.getValue().toString().split(", "));

                                    for (String s : list) {
                                        if (s.contains("{")) {
                                            System.out.println(s);
                                            s = s.replace("{", "");
                                            System.out.println(s);
                                        }
                                        if (s.contains("}")) {
                                            System.out.println(s);
                                            s = s.replace("}", "");
                                        }
                                        System.out.println(s);
                                        String string = s;
                                        String[] parts = string.split("=");
                                        //int date = Integer.parseInt(parts[0]);
                                        String date = parts[0];
                                        float weight = Float.parseFloat(parts[1]);

                                        float date4 = Float.parseFloat(date);
                                        System.out.println("Oi onnen " + date + " ja " + weight + " päivää" + date4);
                                        weightlist.getInstance().add(date4, weight);
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                });
        return weightlist.getInstance().getPaino();
    }

    public void setProfile(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        System.out.println(userid);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("personal", "database");
        docData.put("USER", nestedData);
        db.collection("users").document(userid)
                .set(docData, SetOptions.merge());

    }


    private void BMI(View v){
        if (TextUtils.isEmpty(weightentry.getText().toString())) {
            Toast.makeText(HomePage.this, "Syötä ensin paino",
                    Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(HomePage.this,
                    "Proceed..",
                    Toast.LENGTH_SHORT).show();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            String userid = mAuth.getCurrentUser().getUid();
            mDatabase.child("users").child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        String pituus = (String.valueOf(task.getResult().getValue()));

                        //bmi = (weight/(height*height))
                        //if (bmi <15){
                        //    "Sairaalloinen alipaino"
                        //}else if (15<= bmi <17){
                        //    "Merkittävä alipaino"
                        //}else if (17<=bmi <18.5){
                        //    "Normaalia alhaisempi paino"
                        //}else if (18.5<=bmi <25){
                        //    "Normaali paino"
                        //}else if (25<=bmi <30){
                        //    "Lievä ylipaino"
                        //}else if (30<=bmi <35){
                        //    "Merkittävä ylipaino"
                        //}else if (35<=bmi <40){
                        //    "Vaikea ylipaino"
                        //}else if (bmi>=40){
                        //    "Sairaalloinen ylipaino"
                        //}
                    }
                }
            });
        }
    }




}