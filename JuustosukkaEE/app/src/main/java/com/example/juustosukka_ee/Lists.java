package com.example.juustosukka_ee;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Lists {


    private static ArrayList<Entry> weight = new ArrayList<Entry>();
    private static ArrayList<Entry> steps = new ArrayList<Entry>();
    private static ArrayList<ListInfo> recyclerview = new ArrayList<ListInfo>();
    private static ArrayList<ListInfo> sortedlist = new ArrayList<ListInfo>();
    String Date, weightR, heightR;
    private static Lists Paino = new Lists();

    public static Lists getInstance(){
        return Paino;
    }

    protected Lists() {

    }

    public void addweight(float x, float y){
        weight.add(new Entry(x,y));
    }

    public ArrayList<Entry> getPaino () {
        return weight;
    }

    public ArrayList<ListInfo> getRlist() {
        return recyclerview;
    }

    public ArrayList<ListInfo> getSortedlist() {
        return sortedlist;
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

    public void RecyclerList(String date, String variable, String identifier){
        Date = date;
        if (identifier.equals("weight")){
            if (check(Date)) {
                for (ListInfo c : recyclerview) {
                    if (c.Time.equals(Date)) {
                        c.Weight = variable;
                    }
                }
            } else {
                recyclerview.add(new ListInfo(Date, variable, "-"));
            }
        } else if (identifier.equals("steps")){
            if (check(Date)) {
                for (ListInfo c : recyclerview) {
                    if (c.Time.equals(Date)) {
                        c.Steps = variable;
                    }
                }
            } else {
                recyclerview.add(new ListInfo(Date, "-", variable));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ListInfo> RE(){
        if (sortedlist.size()== 0){
            sortedlist.add(new ListInfo("Pvm:","Paino (kg):","Askeleet:"));
        }
        if (sortedlist.size()== 1){


            ArrayList<Long> s = new ArrayList<Long>();
            for (ListInfo c : recyclerview){
                long date = Long.parseLong(c.Time);
                s.add(date);
            }
            Collections.sort(s, Collections.reverseOrder());
            for (Long l : s){
                String str = String.valueOf(l);
                for (ListInfo olio : recyclerview){
                    if (olio.Time.equals(str)){
                        LocalDate date = LocalDate.ofEpochDay((long) l);
                        String datestr = date.format(DateTimeFormatter.ofPattern("dd.MM."));
                        olio.Time = datestr;
                        sortedlist.add(olio);
                    }
                }

            }
        }
        return sortedlist;
    }


    public boolean check(String date){
        for (ListInfo c : recyclerview){
            if (c.Time.equals(date)){
                return true;
            }
        }
        return false;
    }



    public void clearLists(){
        recyclerview.clear();
        sortedlist.clear();
    }


}
