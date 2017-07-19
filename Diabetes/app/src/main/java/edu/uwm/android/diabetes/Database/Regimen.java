package edu.uwm.android.diabetes.Database;

import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

/**
 * Created by Nate on 7/16/2017.
 */

public class Regimen implements IDatabaseObject{

    private int id;
    private String description;

    public Regimen(){

    }

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
}
