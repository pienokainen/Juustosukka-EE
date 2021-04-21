package com.example.juustosukka_ee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

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

    TextInputEditText cityInput;
    TextView temperatureView, userCityView, weatherStationView;
    Button searchCity;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datapage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cityInput = findViewById(R.id.cityInput);
        searchCity = findViewById(R.id.searchCity);
        temperatureView = findViewById(R.id.temperature);
        weatherStationView = findViewById(R.id.weatherStation);
        userCityView = findViewById(R.id.userCity);
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
}