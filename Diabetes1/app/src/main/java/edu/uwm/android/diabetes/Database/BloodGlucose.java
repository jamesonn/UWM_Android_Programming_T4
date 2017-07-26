package edu.uwm.android.diabetes.Database;

import java.util.Calendar;
import java.util.GregorianCalendar;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;


public class BloodGlucose implements IDatabaseObject{
    private static final String CLASS_ID = "BLOODGLUCOSE";
    private int id;
    private double value;
    private String date ;

    public BloodGlucose(){}

    public BloodGlucose(int id,double value, String date){
        this.id=id;
        this.value = value;
        this.date=date;
    }

    public BloodGlucose(double value, String date){
        this.value = value;
        this.date=date;
    }

    public int getID() {
        return id;
    }

    public double getValue(){
        return this.value;
    }

    public String getDate(){
        return this.date;
    }

    public void setValue(double value){
        this.value = value;
    }

    public void setDate(String date){this.date = date;}

    public void setID(int id) {
        this.id = id;
    }

    public String getClassID(){
        return CLASS_ID;
    }
}
