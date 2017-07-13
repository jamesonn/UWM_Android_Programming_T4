package edu.uwm.android.diabetes;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class BloodGlucose {
    private float value;
    private Calendar date = GregorianCalendar.getInstance();

    public BloodGlucose(float value,int year,int month,int day,int hour){
        this.value = value;
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        date.set(Calendar.MINUTE,0);
        date.set(Calendar.SECOND,0);
    }
    public BloodGlucose(float value,int year,int month,int day,int hour,int minutes){
        this.value = value;
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinutes(minutes);
        date.set(Calendar.SECOND,0);

    }

    public void setValue(float value){
        this.value = value;
    }

    public void setDate(int year,int month,int day,int hour,int minutes){
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinutes(minutes);
        date.set(Calendar.SECOND,0);
    }

    public float getValue(){
        return this.value;
    }

    public Calendar getDate(){
        return date;
    }

    public void setYear(int year){
        date.set(Calendar.YEAR,year);

    }
    //Calender.MONTH works in this range 0-11
    public void setMonth(int month){
        date.set(Calendar.MONTH,month-1);

    }

    public void setDay(int day){
        date.set(Calendar.DAY_OF_MONTH,day);

    }//used for the 24-hour clock
    public void setHour(int hour){
        date.set(Calendar.HOUR_OF_DAY,hour);
    }

    public void setMinutes(int minutes){
        date.set(Calendar.MINUTE,minutes);

    }
    public int getYear(){
        return date.get(Calendar.YEAR);

    }

    public int getMonth(){
        return date.get(Calendar.MONTH);

    }

    public int getDay(){
        return date.get(Calendar.DAY_OF_MONTH);

    }
    public int getHour(){
        return date.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes(){
        return date.get(Calendar.MINUTE);

    }
    public String toString(){
        String output= "The vlaue is:"+ this.value+". It was inserted in: "+ date.getTime().toString();
        return output;
    }

}
