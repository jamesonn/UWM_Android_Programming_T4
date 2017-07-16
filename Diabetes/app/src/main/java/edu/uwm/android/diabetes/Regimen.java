package edu.uwm.android.diabetes;

/**
 * Created by Nate on 7/16/2017.
 */

public class Regimen {

    private int id;
    private String description;

    public Regimen(){

    }

    public int getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
