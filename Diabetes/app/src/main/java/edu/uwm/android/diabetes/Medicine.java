package edu.uwm.android.diabetes;

/**
 * Created by Arham on 7/16/17.
 */
import java.util.Calendar;
import java.util.GregorianCalendar;



public class Medicine {

    private int id;
    private String description;
    private Calendar date = GregorianCalendar.getInstance();


    //constructor 1
    public Medicine(){};

    //constructor 2
    public Medicine(int id,String description, Calendar date){
        this.id = id;
        this.description = description;
        this.date = date;
    }

    //constructor 3
    public Medicine(String description, Calendar date){
        this.id = id;
        this.description = description;
        this.date = date;
    }

    //getters
    public int getId() {
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
    public void setId(int id) {
        this.id = id;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public void setDate(Calendar date){this.date = date;}



}



