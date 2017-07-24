package edu.uwm.android.diabetes.Database;

import java.util.Calendar;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

public class Diet implements IDatabaseObject {
    private static final String CLASS_ID = "DIET";
    private int id;
    private String description;
    private String date;

    public Diet(String description, String date){
        this.description = description;
        this.date = date;
    }

    public int getID() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassID(){
        return CLASS_ID;
    }
}
