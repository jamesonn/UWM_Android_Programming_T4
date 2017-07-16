package edu.uwm.android.diabetes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "Diabetes";

    // We add the tables here
    private static final String TABLE_BLOOD_GLUCOSE = "BloodGlucose";
    private static final String TABLE_EXERCISE= "Exercise";
    private static final String TABLE_DIET= "Diet";
    private static final String TABLE_REGIMEN = "Regimen";
    private static final String TABLE_MEDICINE= "Medicine";

    // We add BloodGlucose Table Columns here
    private static final String BLOOD_GLUCOSE_ID = "id";
    private static final String BLOOD_GLUCOSE_VALUE = "Value";
    private static final String BLOOD_GLUCOSE_DATE = "Date";


    // We add Exercise Table Columns here
    private static final String EXERCISE_ID = "id";
    private static final String EXERCISE_DESCRIPTION = "Description";
    private static final String EXERCISE_DATE = "Date";

    // Diet table columns below
    private static final String DIET_ID = "Id";
    private static final String DIET_DESCRIPTION = "Description";
    private static final String DIET_DATE = "Date";

    //Medecine table columns below
    private static final String MEDICINE_ID = "Id";
    private static final String MEDICINE_DESCRIPTION = "Description";
    private static final String MEDICINE_DATE = "Date";

    //Regimen table columns below
    private static final String REGIMEN_ID = "Id";
    private static final String REGIMEN_DESCRIPTION = "Description";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createBloodGlucoseTable = "create table " + TABLE_BLOOD_GLUCOSE + "("
                + BLOOD_GLUCOSE_ID + " integer primary key autoincrement, "
                + BLOOD_GLUCOSE_VALUE + " real, "
                + BLOOD_GLUCOSE_DATE +" integer);";

        String createExerciseTable = "create table " + TABLE_EXERCISE + "("
                + EXERCISE_ID + " integer primary key autoincrement, "
                + EXERCISE_DESCRIPTION+ " text not null, "
                + EXERCISE_DATE+" integer);";

        String createDietTable = "Create table" + TABLE_DIET + "("+
                DIET_ID + " integer primary key autoincrement, "
                + DIET_DESCRIPTION+ " Text not null, "
                + DIET_DATE+" integer);";

        String createMedecineTable = "Create table" + TABLE_MEDICINE + "("+
                MEDICINE_ID + " integer primary key autoincrement, "
                + MEDICINE_DESCRIPTION+ " Text not null, "
                + MEDICINE_DATE+" integer);";

        String createRegimenTable = "create table " + TABLE_REGIMEN + "("
                + REGIMEN_ID + " integer primary key autoincrement, "
                + REGIMEN_DESCRIPTION+ " text not null);";


        db.execSQL(createBloodGlucoseTable);
        db.execSQL(createExerciseTable);
        db.execSQL(createDietTable);
        db.execSQL(createRegimenTable);;
        db.execSQL(createMedecineTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOOD_GLUCOSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGIMEN);
        onCreate(db);


    }

    //to add BloodGlucose object to TABLE_BloodGlucose
    public void addBloodGlucose(BloodGlucose BGL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BLOOD_GLUCOSE_VALUE, BGL.getValue());//value
        values.put(BLOOD_GLUCOSE_DATE, BGL.getDate().getTimeInMillis()); //date in milliseconds

        db.insert(TABLE_BLOOD_GLUCOSE, null, values);//Inserting Row
        db.close(); // Closing database connection
    }

    public void addDiet(Diet diet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DIET_DESCRIPTION, diet.getDescription());//value
        values.put(DIET_DATE, diet.getDate().getTimeInMillis()); //date in milliseconds

        db.insert(TABLE_DIET, null, values);//Inserting Row
        db.close(); // Closing database connection
    }

    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EXERCISE_DESCRIPTION, exercise.getDescription());
        values.put(EXERCISE_DATE, exercise.getDate().getTimeInMillis());
        db.insert(TABLE_EXERCISE, null, values);
        db.close();
    }


    public void addRegimen(Regimen regimen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();;
        values.put(REGIMEN_DESCRIPTION, regimen.getDescription());
        db.insert(TABLE_REGIMEN, null, values);
        db.close();
    }

    public void addMedicine(Medicine medicine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();;
        values.put(REGIMEN_DESCRIPTION, medicine.getDescription());
        values.put(REGIMEN_DESCRIPTION, medicine.getDate().getTimeInMillis());
        db.insert(TABLE_REGIMEN, null, values);
        db.close();
    }

    public void deleteBloodGlucose(String bloodGlucoseID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BLOOD_GLUCOSE, BLOOD_GLUCOSE_ID+ "=" + bloodGlucoseID , null);
        db.close();
    }

    public void deleteDiet(String dietID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DIET, DIET_ID+ "=" + dietID , null);
        db.close();
    }

    public void deleteExercise(String exerciseID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, EXERCISE_ID+ "=" + exerciseID , null);
        db.close();
    }

    public void deleteRegimen(String regimenID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REGIMEN, REGIMEN_ID+ "=" + regimenID , null);
        db.close();
    }

    public void deleteMedicine(String medecineID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINE, MEDICINE_ID+ "=" + medecineID , null);
        db.close();
    }

    public ArrayList<Exercise> getAllExercises(){
        ArrayList<Exercise> output = new ArrayList<>();//the holder
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXERCISE;
        Cursor cursor = db.rawQuery(query,null);//pointer

        if (cursor.moveToFirst()) {//if not empty, move to the first set of elements
            do {
                Exercise exercise = new Exercise();//this object needs id, description and date

                //we start with date
                long date = cursor.getInt(3); //returns long int
                Calendar cal = new GregorianCalendar();//Calendar object for setDate()
                cal.setTimeInMillis(date);//now the calendar object holds our date

                exercise.setDate(cal);//done setting date field in Exercise
                exercise.setId(cursor.getInt(0));//done setting id
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