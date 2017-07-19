package edu.uwm.android.diabetes.Database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

/**
 * Created by Nate on 7/16/2017.
 */

public class Regimen implements IDatabaseObject{
    private static final String CLASS_ID = "REGIMEN";
    private int id;
    private String description;
    private Calendar date = GregorianCalendar.getInstance();

    public Regimen(){ }

    public int getID(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDate(Calendar date){this.date = date;}

    public Calendar getDate(){
        return this.date;
    }

    public String getClassID(){
        return CLASS_ID;
    }
}
