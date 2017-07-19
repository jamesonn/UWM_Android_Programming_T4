package edu.uwm.android.diabetes.Database;


import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;


public class Exercise implements IDatabaseObject{

    private int id;
    private String description;
    private Calendar date = GregorianCalendar.getInstance();


    //constructor 1
    public Exercise(){};
    //constructor 2

    public Exercise(int id,String description, Calendar date){
        this.id = id;
        this.description = description;
        this.date = date;
    }
    //constructor 3
    public Exercise(String description, Calendar date){
        this.id = id;
        this.description = description;
        this.date = date;
    }

    //getters
    public int getID() {
        return id;
    }
    public String getDescription(){
        return this.description;
    }
    //Output example: Thu Jul 13 15:00:00 CDT 2017 after using
    //getDate().getTime().toString;
    public Calendar getDate(){
        return this.date;
    }

    //setters
    public void setID(int id) {
        this.id = id;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public void setDate(Calendar date){this.date = date;}



}
