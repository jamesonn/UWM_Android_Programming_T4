package edu.uwm.android.diabetes.Database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;


public class Regimen implements IDatabaseObject{
    private static final String CLASS_ID = "REGIMEN";
    private int id;
    private String exerciseDescription;

    public String getDietDescription() {
        return dietDescription;
    }

    public void setDietDescription(String dietDescription) {
        this.dietDescription = dietDescription;
    }

    private String dietDescription;
    private String date ;

    public Regimen(){ }

    public int getID(){
        return id;
    }

    public String getExerciseDescription(){
        return exerciseDescription;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setExerciseDescription(String exerciseDescription){
        this.exerciseDescription = exerciseDescription;
    }

    public void setDate(String date){this.date = date;}

    public String getDate(){
        return this.date;
    }

    public String getClassID(){
        return CLASS_ID;
    }
}
