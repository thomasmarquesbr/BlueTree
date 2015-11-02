package com.example.usuario.app;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Marina on 02/09/2015.
 */
public class ApplicationXML {

    String start, end;
    ArrayList<ActivityXML> activities;

    public ApplicationXML(String start, String end){
        this.start = start;
        this.end = end;
        activities = new ArrayList<>();
    }

    public ApplicationXML(){
        activities = new ArrayList<>();
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public void addActivity(ActivityXML activity){
        this.activities.add(activity);
    }

    public ActivityXML getActivity(int index){
        return this.activities.get(index);
    }

    public ArrayList<ActivityXML> getListActivity(){
        return this.activities;
    }



}
