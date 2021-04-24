package com.example.juustosukka_ee;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class RecyclerListClass {



    private static ArrayList<ListInfo> recyclerviewlist = new ArrayList<ListInfo>();
    private static ArrayList<ListInfo> sortedlist = new ArrayList<ListInfo>();
    String Date;

    private static RecyclerListClass historialista = new RecyclerListClass();

    public static RecyclerListClass getInstance(){
        return historialista;
    }

    protected RecyclerListClass() {

    }

    public ArrayList<ListInfo> getRlist() {
        return recyclerviewlist;
    }

    public ArrayList<ListInfo> getSortedlist() {
        return sortedlist;
    }

    public void RecyclerList(String date, String variable, String identifier){
        Date = date;
        if (identifier.equals("weight")){
            if (check(Date)) {
                for (ListInfo c : recyclerviewlist) {
                    if (c.Time.equals(Date)) {
                        c.Weight = variable;
                    }
                }
            } else {
                recyclerviewlist.add(new ListInfo(Date, variable, "-"));
            }
        } else if (identifier.equals("steps")){
            if (check(Date)) {
                for (ListInfo c : recyclerviewlist) {
                    if (c.Time.equals(Date)) {
                        c.Steps = variable;
                    }
                }
            } else {
                recyclerviewlist.add(new ListInfo(Date, "-", variable));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ListInfo> GetSortedRList(){
        if (sortedlist.size()== 0){
            sortedlist.add(new ListInfo("Pvm:","Paino (kg):","Askeleet:"));
        }
        if (sortedlist.size()== 1){


            ArrayList<Long> s = new ArrayList<Long>();
            for (ListInfo c : recyclerviewlist){
                long date = Long.parseLong(c.Time);
                s.add(date);
            }
            Collections.sort(s, Collections.reverseOrder());
            for (Long l : s){
                String str = String.valueOf(l);
                for (ListInfo olio : recyclerviewlist){
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
        for (ListInfo c : recyclerviewlist){
            if (c.Time.equals(date)){
                return true;
            }
        }
        return false;
    }



    public void clearLists(){
        recyclerviewlist.clear();
        sortedlist.clear();
    }
}
