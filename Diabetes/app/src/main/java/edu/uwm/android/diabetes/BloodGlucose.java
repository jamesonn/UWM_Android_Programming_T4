package edu.uwm.android.diabetes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class BloodGlucose {

    private int id;
    private double value;
    private Calendar date = GregorianCalendar.getInstance();


    public BloodGlucose(){};

    public BloodGlucose(int id,double value, Calendar date){
        this.id=id;
        this.value = value;
        this.date=date;

    }

    public BloodGlucose(double value, Calendar date){
        this.id=id;
        this.value = value;
        this.date=date;

    }

    public int getId() {
        return id;
    }

    public double getValue(){
        return this.value;
    }

    public Calendar getDate(){
        return this.date;
    }


    public void setValue(double value){
        this.value = value;
    }

    public void setDate(Calendar date){this.date = date;}

    public void setId(int id) {
        this.id = id;
    }


}
