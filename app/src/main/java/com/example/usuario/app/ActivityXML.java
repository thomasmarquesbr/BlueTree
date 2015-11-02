package com.example.usuario.app;

/**
 * Created by Marina on 02/09/2015.
 */
public class ActivityXML {

    private String type;
    private int id;
    private int begin;
    private int duration;
    private boolean released;

    public ActivityXML(String type, int id){
        this.type = type;
        this.id = id;
        this.released = false;
    }

    public void setType(String newType){
        this.type = newType;
    }

    public void setId(int newId){
        this.id = newId;
    }

    public void setBegin(int newBegin){
        this.begin = newBegin;
    }

    public void setDuration(int newDuration){
        this.duration = newDuration;
    }

    public String getType(){
        return this.type;
    }

    public int getId(){
        return this.id;
    }

    public int getBegin(){
        return this.begin;
    }

    public int getDuration(){
        return this.duration;
    }

    public boolean getReleased(){
        return this.released;
    }

    public void setReleased(boolean isReleased){
        this.released = isReleased;
    }
}
