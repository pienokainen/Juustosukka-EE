package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HomePage extends AppCompatActivity {

    Button nappi;
    EditText weightentry;
    int luku;
    private FirebaseAuth mAuth;
    LineChart mpstepschart;
    LineChart mpweightchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mAuth = FirebaseAuth.getInstance();
        nappi = findViewById(R.id.plusnappi);
        weightentry = findViewById(R.id.weight);
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
        mpstepschart = findViewById(R.id.stepschart);
        LineDataSet lineDataSet1 = new LineDataSet(weightData(), "Weight");
        LineDataSet lineDataSet2 = new LineDataSet(stepsData(), "Steps");
        ArrayList<ILineDataSet> weightdata = new ArrayList<>();
        ArrayList<ILineDataSet> stepsdata = new ArrayList<>();
        weightdata.add(lineDataSet1);
        stepsdata.add(lineDataSet2);
        LineData data1 = new LineData(weightdata);
        mpweightchart.setData(data1);
        mpweightchart.invalidate();
        mpweightchart.setDrawBorders(true);
        mpweightchart.setBorderColor(Color.BLACK);
        mpweightchart.setBorderWidth(1);
        //mpweightchart.setBackgroundColor(Color.CYAN); //tästä väriä taustalle jos halajaa
        XAxis xAxis1 = mpweightchart.getXAxis();
        YAxis yAxisr1 = mpweightchart.getAxisRight();
        YAxis yAxisl1 = mpweightchart.getAxisLeft();
        LineData data2 = new LineData(stepsdata);
        mpstepschart.setData(data2);
        mpstepschart.invalidate();
        mpstepschart.setDrawBorders(true);
        mpstepschart.setBorderColor(Color.BLACK);
        mpstepschart.setBorderWidth(1);
        //mpstepschart.setBackgroundColor(Color.CYAN);
        XAxis xAxis2 = mpstepschart.getXAxis();
        xAxis1.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
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

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }


        });


    }



    private ArrayList<Entry> stepsData(){
        ArrayList<Entry> stepslist = new ArrayList<Entry>();
        stepslist.add(new Entry(0,2000));
        stepslist.add(new Entry(1,2300));
        stepslist.add(new Entry(2,1990));
        stepslist.add(new Entry(3,1800));
        stepslist.add(new Entry(4,2222));
        stepslist.add(new Entry(5,1999));
        return stepslist;
    }

    private ArrayList<Entry> weightData(){
        ArrayList<Entry> weightlist = new ArrayList<Entry>();
        weightlist.add(new Entry(1,29));
        weightlist.add(new Entry(2,15));
        weightlist.add(new Entry(3,22));
        weightlist.add(new Entry(4,32));
        weightlist.add(new Entry(5,25));
        weightlist.add(new Entry(6,23));
        weightlist.add(new Entry(7,32));
        return weightlist;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Nappi(View v){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate localDate = LocalDate.now();

        String userid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> nestedData = new HashMap<>();
        int weightI = Integer.parseInt(weightentry.getText().toString());
        nestedData.put(dtf.format(localDate), weightI);
        docData.put("Paino", nestedData);


        db.collection("users").document(userid)
                .set(docData, SetOptions.merge());

    }



}