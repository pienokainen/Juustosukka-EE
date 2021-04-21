package com.example.juustosukka_ee;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;



public class weightlist {


    private static ArrayList<Entry> weight = new ArrayList<Entry>();
    private static ArrayList<Entry> steps = new ArrayList<Entry>();

    private static weightlist Paino = new weightlist();

    public static weightlist getInstance(){
        return Paino;
    }

    protected weightlist() {

    }

    public void add(float x, float y){
        System.out.println(x+" JA "+ y);
        weight.add(new Entry(x,y));
        for (Entry c : weight){
            System.out.println(c+ "on");
            System.out.println(weight.size()+" = KOKOO");
        }
    }

    public ArrayList<Entry> getPaino () {
        return weight;
    }
    public void addsteps(float x, float y){
        System.out.println(x+" JA "+ y);
        steps.add(new Entry(x,y));
        for (Entry c : steps){
            System.out.println(c+ "on");
            System.out.println(steps.size()+" = KOKOO");
        }
    }

    public ArrayList<Entry> getSteps () {
        return steps;
    }
    public void empty () {
        weight.clear();
        steps.clear();
    }
}
