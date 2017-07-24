package edu.uwm.android.diabetes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.uwm.android.diabetes.Constants;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createBloodGlucoseTable = " create table " + Constants.TABLE_BLOOD_GLUCOSE + " ("
                + Constants.BLOOD_GLUCOSE_ID + " integer primary key autoincrement, "
                + Constants.BLOOD_GLUCOSE_VALUE + " real, "
                + Constants.BLOOD_GLUCOSE_DATE +" text);";

        String createExerciseTable = " create table " + Constants.TABLE_EXERCISE + " ("
                + Constants.EXERCISE_ID + " integer primary key autoincrement, "
                + Constants.EXERCISE_DESCRIPTION+ " text not null, "
                + Constants.EXERCISE_DATE+" text);";

        String createDietTable = " Create table " + Constants.TABLE_DIET + " ("+
                Constants.DIET_ID + " integer primary key autoincrement, "
                + Constants.DIET_DESCRIPTION+ " Text not null); ";
               // + Constants.DIET_DATE+" integer);";

        String createMedicineTable = " Create table " + Constants.TABLE_MEDICINE + " ("+
                Constants.MEDICINE_ID + " integer primary key autoincrement, "
                + Constants.MEDICINE_DESCRIPTION+ " Text not null, "
                + Constants.MEDICINE_DATE+" integer);";

        String createRegimenTable = " create table " + Constants.TABLE_REGIMEN + " ("
                + Constants.REGIMEN_ID + " integer primary key autoincrement, "
                + Constants.REGIMEN_DESCRIPTION+ " text not null);";

        db.execSQL(createBloodGlucoseTable);
        db.execSQL(createExerciseTable);
        db.execSQL(createDietTable);
        db.execSQL(createMedicineTable);
        db.execSQL(createRegimenTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_BLOOD_GLUCOSE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_REGIMEN);
        onCreate(db);
    }

    public void add(IDatabaseObject object){
        if(object != null) {
            String classType = object.getClassID();

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            System.out.println("The type of the class is "+ classType);

            switch (classType) {
                case Constants.BLOODGLUCOSE_CLASS:
                    BloodGlucose bgl = (BloodGlucose) object;

                    values.put(Constants.BLOOD_GLUCOSE_VALUE, bgl.getValue());
                    values.put(Constants.BLOOD_GLUCOSE_DATE, bgl.getDate().toString());
                    db.insert(Constants.TABLE_BLOOD_GLUCOSE, null, values);
                    break;
                case Constants.DIET_CLASS:
                    Diet diet = (Diet) object;
                    values.put(Constants.DIET_DESCRIPTION, diet.getDescription());
                    values.put(Constants.DIET_DATE, diet.getDate().getTimeInMillis());
                    db.insert(Constants.TABLE_DIET, null, values);
                    break;
                case Constants.EXERCISE_CLASS:
                    System.out.println("-----Exercise case  here");
                    Exercise exercise = (Exercise) object;
                    values.put(Constants.EXERCISE_DESCRIPTION, exercise.getDescription());
                    //values.put(Constants.EXERCISE_DATE, exercise.getDate().toString());
                    db.insert(Constants.TABLE_EXERCISE, null, values);
                    break;
                case Constants.MEDICINE_CLASS:
                    Medicine medicine = (Medicine) object;

                    values.put(Constants.MEDICINE_DESCRIPTION, medicine.getDescription());
                    values.put(Constants.MEDICINE_DATE, medicine.getDate().getTimeInMillis());
                    db.insert(Constants.TABLE_MEDICINE, null, values);
                    break;
                case Constants.REGIMEN_CLASS:
                    System.out.println("-----Regimen case  here");
                    Regimen regimen = (Regimen) object;
                    values.put(Constants.REGIMEN_DESCRIPTION, regimen.getDescription());
                    db.insert(Constants.TABLE_REGIMEN, null, values);
                    System.out.println("-----------------------> Data inserted in Regimen Table!!!!!!!!!!!!!!!!!!!");
                    break;
                default:
                    //TODO Throw an error? Should never happen
                    break;
            }
            db.close(); // Closing database connection
        }
    }

    public void delete(IDatabaseObject object){
        if(object != null) {
            String classType = object.getClassID();

            SQLiteDatabase db = this.getWritableDatabase();

            switch (classType) {
                case Constants.BLOODGLUCOSE_CLASS:
                    BloodGlucose bgl = (BloodGlucose) object;

                    db.delete(Constants.TABLE_BLOOD_GLUCOSE, Constants.BLOOD_GLUCOSE_ID+ "=" + bgl.getID() , null);
                    break;
                case Constants.DIET_CLASS:
                    Diet diet = (Diet) object;

                    db.delete(Constants.TABLE_DIET, Constants.DIET_ID+ "=" + diet.getID() , null);
                    break;
                case Constants.EXERCISE_CLASS:
                    Exercise exercise = (Exercise) object;

                    db.delete(Constants.TABLE_EXERCISE, Constants.EXERCISE_ID+ "=" + exercise.getID() , null);
                    break;
                case Constants.MEDICINE_CLASS:
                    Medicine medicine = (Medicine) object;

                    db.delete(Constants.TABLE_MEDICINE, Constants.MEDICINE_ID+ "=" + medicine.getID() , null);
                    break;
                case Constants.REGIMEN_CLASS:
                    Regimen regimen = (Regimen) object;

                    db.delete(Constants.TABLE_REGIMEN, Constants.REGIMEN_ID+ "=" + regimen.getID() , null);
                    break;
                default:
                    //TODO Throw an error? Should never happen
                    break;
            }
            db.close(); // Closing database connection
        }
    }


    public Cursor getData(IDatabaseObject object){
        Cursor result = null;
        if(object != null) {
            String classType = object.getClassID();

            SQLiteDatabase db = this.getWritableDatabase();
             switch (classType) {
                case Constants.BLOODGLUCOSE_CLASS:
                    result = db.rawQuery("Select * from " +Constants.TABLE_BLOOD_GLUCOSE, null);
                    break;
                case Constants.DIET_CLASS:
                    result = db.rawQuery("Select * from " +Constants.TABLE_DIET, null);
                    break;
                case Constants.EXERCISE_CLASS:
                    result = db.rawQuery("Select * from " +Constants.TABLE_EXERCISE, null);
                    break;
                case Constants.MEDICINE_CLASS:
                    result = db.rawQuery("Select * from " +Constants.TABLE_MEDICINE, null);
                    break;
                case Constants.REGIMEN_CLASS:
                    result = db.rawQuery("Select * from " +Constants.TABLE_REGIMEN, null);
                    break;
                default:
                    //TODO Throw an error? Should never happen
                    break;
            }

        }
        return result;
    }


    public ArrayList<Exercise> getAllExercises(){
        ArrayList<Exercise> output = new ArrayList<>();//the holder
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + Constants.TABLE_EXERCISE;
        Cursor cursor = db.rawQuery(query,null);//pointer

        if (cursor.moveToFirst()) {//if not empty, move to the first set of elements
            do {
                Exercise exercise = new Exercise();//this object needs id, description and date

                //we start with date
                long date = cursor.getLong(3); //returns long
                Calendar cal = new GregorianCalendar();//Calendar object for setDate()
                cal.setTimeInMillis(date);//now the calendar object holds our date

                exercise.setDate(cal);//done setting date field in Exercise
                exercise.setID(cursor.getInt(0));//done setting id
                exercise.setDescription(cursor.getString(1));//done setting Description

                //now exercise has id, description and date, we can add it to the ArrayList

                // Adding Exercise to list
                output.add(exercise);
            } while (cursor.moveToNext());//go to the next row
        }
        db.close();
        return output;
    }
}