package edu.uwm.android.diabetes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
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

        String createLoginTable = " create table " + Constants.TABLE_LOGIN + " ("
                + Constants.LOGIN_USERNAME + " text primary key, "
                + Constants.LOGIN_PASSWORD +" text);";

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

        String createMedicineTable = " Create table " + Constants.TABLE_MEDICINE + " ("+
                Constants.MEDICINE_ID + " integer primary key autoincrement, "
                + Constants.MEDICINE_DESCRIPTION+ " Text not null, "
                + Constants.MEDICINE_DATE+" Text);";

        String createRegimenTable = " create table " + Constants.TABLE_REGIMEN + " ("
                + Constants.REGIMEN_ID + " integer primary key autoincrement, "
                + Constants.REGIMEN_DESCRIPTION+ " text not null);";


        db.execSQL(createLoginTable);
        db.execSQL(createBloodGlucoseTable);
        db.execSQL(createExerciseTable);
        db.execSQL(createDietTable);
        db.execSQL(createMedicineTable);
        db.execSQL(createRegimenTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_BLOOD_GLUCOSE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_REGIMEN);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LOGIN_USERNAME, user.getUserName());
        values.put(Constants.LOGIN_PASSWORD, user.getPassword());
        db.insert(Constants.TABLE_LOGIN, null, values);
    }

    public User getUserByUserName (String userName){
        Cursor result;
        User userFound = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.rawQuery("Select * from " +Constants.TABLE_LOGIN+ " WHERE " + Constants.LOGIN_USERNAME+ " = '" +userName+"'", null);
        if (result.getCount() == 0) {
            System.out.println("No Records");
        } else {
            while (result.moveToNext()) {
                userFound.setUserName(result.getString(0));
                userFound.setPassword(result.getString(1));
            }
        }
        return userFound;
    }

    public boolean isUserRegistered (String userName){
        boolean found = false;
        Cursor result;
        User userFound = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.rawQuery("Select * from " +Constants.TABLE_LOGIN+ " WHERE " + Constants.LOGIN_USERNAME+ " = '" +userName+"'", null);
        if (result.getCount() == 0) {
            System.out.println("No Records");
            found = false;
        } else {
            found = true;
            while (result.moveToNext()) {
                userFound.setUserName(result.getString(0));
                userFound.setPassword(result.getString(1));
            }
        }

        return found;
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
                    values.put(Constants.DIET_DATE, diet.getDate().toString());
                    db.insert(Constants.TABLE_DIET, null, values);
                    break;
                case Constants.EXERCISE_CLASS:
                    Exercise exercise = (Exercise) object;
                    values.put(Constants.EXERCISE_DESCRIPTION, exercise.getDescription());
                    values.put(Constants.EXERCISE_DATE, exercise.getDate());
                    db.insert(Constants.TABLE_EXERCISE, null, values);
                    break;
                case Constants.MEDICINE_CLASS:
                    Medicine medicine = (Medicine) object;

                    values.put(Constants.MEDICINE_DESCRIPTION, medicine.getDescription());
                    values.put(Constants.MEDICINE_DATE, medicine.getDate().toString());
                    db.insert(Constants.TABLE_MEDICINE, null, values);
                    break;
                case Constants.REGIMEN_CLASS:
                    Regimen regimen = (Regimen) object;
                    values.put(Constants.REGIMEN_DESCRIPTION, regimen.getDescription());
                    db.insert(Constants.TABLE_REGIMEN, null, values);
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

}