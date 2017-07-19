package edu.uwm.android.diabetes.Interfaces;

import java.util.Calendar;

/**
 * Created by Nate on 7/18/2017.
 */

public interface IDatabaseObject {
    int getID();
    void setID(int id);
    Calendar getDate();
    void setDate(Calendar date);

}
