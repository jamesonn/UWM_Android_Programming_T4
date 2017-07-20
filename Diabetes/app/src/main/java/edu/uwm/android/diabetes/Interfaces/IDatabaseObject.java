package edu.uwm.android.diabetes.Interfaces;

import java.util.Calendar;

public interface IDatabaseObject {

    int getID();
    void setID(int id);
    Calendar getDate();
    void setDate(Calendar date);
    String getClassID();
}
