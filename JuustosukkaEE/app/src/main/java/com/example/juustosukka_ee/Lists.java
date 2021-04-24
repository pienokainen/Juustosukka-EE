package com.example.juustosukka_ee;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;


public class Lists {


    private static ArrayList<Entry> weight = new ArrayList<Entry>();
    private static ArrayList<Entry> steps = new ArrayList<Entry>();
    String Date, weightR, heightR;
    private static Lists Listat = new Lists();

    public static Lists getInstance(){
        return Listat;
    }

    protected Lists() {

    }

    public void addweight(float x, float y){
        weight.add(new Entry(x,y));
    }

    public ArrayList<Entry> getPaino () {
        return weight;
    }





    public void addsteps(float x, float y){
        steps.add(new Entry(x,y));
    }

    public ArrayList<Entry> getSteps () {
        return steps;
    }
    public void empty () {
        weight.clear();
        steps.clear();
    }

    public ArrayList<Entry> sortlist (ArrayList<Entry> lista) {
        ArrayList<Float> days = new ArrayList<Float>();
        for (Entry c : lista){
            float x = c.getX();
            days.add(x);
        }
        Collections.sort(days);

        ArrayList<Entry> newlist = new ArrayList<Entry>();
        for (Float d : days){
            for(Entry e : lista){
                if (e.getX() == d){
                    newlist.add(new Entry(e.getX(),e.getY()));
                }
            }
        }

        return newlist;
    }




}
