package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class DataPage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText weightentry, heightentry;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    TextInputEditText cityInput;
    TextView temperatureView, userCityView, weatherStationView, bmitext, bmiresult;
    Button searchCity;
    String city, currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datapage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }
        cityInput = findViewById(R.id.cityInput);
        searchCity = findViewById(R.id.searchCity);
        temperatureView = findViewById(R.id.temperature);
        weatherStationView = findViewById(R.id.weatherStation);
        userCityView = findViewById(R.id.userCity);
        bmitext = findViewById(R.id.bmitext);
        bmiresult = findViewById(R.id.bmiresult);
        weightentry = findViewById(R.id.bmiweight);
        heightentry = findViewById(R.id.bmiheight);
        setEntries();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);
        bottomNavigationView.setSelectedItemId(R.id.data);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.data:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
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

    public void searchWeatherData(View v) {
        try {
            city = cityInput.getText().toString();
            //Reading XML from Ilmatieteenlaitos
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();

            //Fetching and formatting user's time
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:00:00", Locale.getDefault()).format(new Date());

            URL url = new URL("http://opendata.fmi.fi/wfs/fin?service=WFS" +
                    "&version=2.0.0&request=GetFeature" +
                    "&storedquery_id=fmi::observations::weather::timevaluepair" +
                    "&place=" + city +
                    "&starttime=" + currentDate + "T" + currentTime + "Z" +
                    "&endtime="  + currentDate + "T" + currentTime + "Z" +
                    "&maxlocations=1&parameters=temperature&");
            InputStream stream = url.openStream();

            // Displaying data on datapage
            Document doc = docBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("wfs:member");
            Node node = nList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String temperature = element.getElementsByTagName("wml2:value").item(0).getTextContent();
                String weatherStation = element.getElementsByTagName("gml:name").item(0).getTextContent();
                temperatureView.setText(temperature + "°C");
                userCityView.setText(city);
                weatherStationView.setText(weatherStation);
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void CalculateBMI(View v) {
        if (TextUtils.isEmpty(weightentry.getText().toString())) {
            Toast.makeText(DataPage.this, "Syötä ensin paino",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(heightentry.getText().toString())) {
                Toast.makeText(DataPage.this, "Syötä ensin pituus",
                        Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DataPage.this,
                "Tiedot syötetty onnistuneesti",
                Toast.LENGTH_SHORT).show();
            double weight = Double.parseDouble(weightentry.getText().toString().replace("kg", ""));
            double height = Double.parseDouble(heightentry.getText().toString().replace("cm", ""));
            height /= 100;
            double bmi = (weight/(height*height));
            bmi = (double) Math.round(bmi * 100) / 100;
            if (bmi <15){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Sairaalloinen alipaino");
            }else if (15<= bmi && bmi<17){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Merkittävä alipaino");
            }else if (17<=bmi && bmi <18.5){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Normaalia alhaisempi paino");
            }else if (18.5<=bmi && bmi <25){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Normaali paino");
            }else if (25<=bmi && bmi<30){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Lievä ylipaino");
            }else if (30<=bmi && bmi<35){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Merkittävä ylipaino");
            }else if (35<=bmi && bmi<40){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Vaikea ylipaino");
            }else if (bmi>=40){
                bmiresult.setText(String.valueOf(bmi));
                bmitext.setText("Sairaalloinen ylipaino");
            }

        }

    }

    public void setEntries(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(currentUserID).child("pituus").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String pituus = (String.valueOf(task.getResult().getValue()));
                    heightentry.setText(pituus+"cm");
                }
            }
        });

        mDatabase.child("Users").child(currentUserID).child("paino").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String paino = (String.valueOf(task.getResult().getValue()));
                    weightentry.setText(paino+"kg");
                }
            }
        });
    }




}