package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class HomePage extends AppCompatActivity {
    EditText weightentry;
    EditText stepsentry;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    LineChart mpstepschart;
    LineChart mpweightchart;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setProfile();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }
        weightentry = findViewById(R.id.weight);
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
        ArrayList<Entry> weightlist = weightData();
        ArrayList<Entry> sortedwlist = Lists.getInstance().sortlist(weightlist);
        LineDataSet lineDataSet1 = new LineDataSet(sortedwlist, "Paino");
        ArrayList<ILineDataSet> weightdata = new ArrayList<>();
        weightdata.add(lineDataSet1);
        LineData data1 = new LineData(weightdata);
        mpweightchart.setData(data1);
        mpweightchart.invalidate();
        mpweightchart.setDrawBorders(true);
        mpweightchart.setBorderColor(Color.BLACK);
        mpweightchart.setBorderWidth(1);
        XAxis xAxis1 = mpweightchart.getXAxis();
        YAxis yAxisr1 = mpweightchart.getAxisRight();
        YAxis yAxisl1 = mpweightchart.getAxisLeft();

        mpstepschart = findViewById(R.id.stepschart);
        ArrayList<Entry> stepslist = stepsData();
        ArrayList<Entry> sortedslist = Lists.getInstance().sortlist(stepslist);
        LineDataSet lineDataSet2 = new LineDataSet(sortedslist, "Askeleet");
        ArrayList<ILineDataSet> stepsdata = new ArrayList<>();
        stepsdata.add(lineDataSet2);
        LineData data2 = new LineData(stepsdata);
        mpstepschart.setData(data2);
        mpstepschart.invalidate();
        mpstepschart.setDrawBorders(true);
        mpstepschart.setBorderColor(Color.BLACK);
        mpstepschart.setBorderWidth(1);

        XAxis xAxis2 = mpstepschart.getXAxis();
        YAxis yAxisl2 = mpstepschart.getAxisLeft();
        YAxis yAxisr2 = mpstepschart.getAxisRight();
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

                double round = (double) Math.round(value * 100) / 100;
                return round + "kg";
            }


        });

        yAxisr1.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {

                double round = (double) Math.round(value * 100) / 100;
                return round + "kg";
            }


        });

        yAxisl2.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                String round = String.valueOf((int) value);
                return round;
            }
        });

        yAxisr2.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                String round = String.valueOf((int) value);
                return round;
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
        Lists.getInstance().empty();
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
        for (Entry c : Lists.getInstance().getPaino()){
            float x = c.getX();
            float y = c.getY();

            weighlist.add(c);
        }
        weighlist.remove(0);
        return weighlist;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setWeightentry(View v){
        if (weightentry.getText().toString().length() > 0) {
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
            weightentry.getText().clear();

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Users").child(currentUserID).child("paino").setValue(weightI);
            Toast.makeText(HomePage.this, "Painon tallennus onnistui",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HomePage.this, "Syötä ensin paino",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStepsentry(View v) {
        if (stepsentry.getText().toString().length() > 0){
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
            stepsentry.getText().clear();
            Toast.makeText(HomePage.this, "Askeleiden tallennus onnistui",
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(HomePage.this, "Syötä ensin askeleiden määrä",
                    Toast.LENGTH_SHORT).show();
        }
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
                                    Lists.getInstance().addsteps(date4,weight);
                                }
                            }
                        }

                    }
                });
        return Lists.getInstance().getSteps();
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
                                    List<String> list = new ArrayList<String>();
                                    list = Arrays.asList(entry.getValue().toString().split(", "));

                                    for (String s : list) {
                                        if (s.contains("{")) {
                                            s = s.replace("{", "");
                                        }
                                        if (s.contains("}")) {
                                            s = s.replace("}", "");
                                        }
                                        String string = s;
                                        String[] parts = string.split("=");
                                        //int date = Integer.parseInt(parts[0]);
                                        String date = parts[0];
                                        float weight = Float.parseFloat(parts[1]);

                                        float date4 = Float.parseFloat(date);
                                        Lists.getInstance().addweight(date4, weight);
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                });
        return Lists.getInstance().getPaino();
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

}