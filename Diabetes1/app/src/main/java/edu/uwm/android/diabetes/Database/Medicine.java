package edu.uwm.android.diabetes.Database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

public class Medicine implements IDatabaseObject {
    private static final String CLASS_ID = "MEDICINE";
    private int id;
    private String description;
    private String date;

    public Medicine(){}

    public Medicine(int id,String description, String date){
        this.id = id;
        this.description = description;
        this.date = date;
    }

    //constructor 3
    public Medicine(String description, String date){
        this.id = id;
        this.description = description;
        this.date = date;
    }

    public int getID() {
        return id;
    }
    public String getDescription(){
        return this.description;
    }
    public String getDate(){
        return this.date;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDate(String date){this.date = date;}

    public String getClassID(){
        return CLASS_ID;
    }

}



