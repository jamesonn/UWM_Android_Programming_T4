package edu.uwm.android.diabetes;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Rafa on 7/16/2017.
 */

public class Diet {

    private int id;
    private String description;
    private Calendar date;

    public Diet(String description, Calendar date){
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
