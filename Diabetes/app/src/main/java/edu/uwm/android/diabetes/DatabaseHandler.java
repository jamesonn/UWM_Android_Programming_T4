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
    private static final String TABLE_BloodGlucose = "Blood Glucose";
    private static final String TABLE_Exercise= "Exercise";
    private static final String TABLE_Regimen = "Regimen";
    private static final String TABLE_Medicine= "Medicine";

    // We add BloodGlucose Table Columns here
    private static final String KEY_BloodGlucoseID = "id";
    private static final String KEY_BloodGlucoseValue = "Value";
    private static final String KEY_BloodGlucoseDate = "Date";


    // We add Exercise Table Columns here
    private static final String KEY_ExerciseID = "id";
    private static final String KEY_ExerciseDescription = "Description";
    private static final String KEY_ExerciseDate = "Date";

    private static final String KEY_RegimenID = "id";
    private static final String KEY_RegimenDescription = "Description";

    private static final String KEY_MedicineID = "id";
    private static final String KEY_MedicineDescription = "Description";
    private static final String KEY_MedicineDate = "Date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_BloodGlucose_table = "create table " + TABLE_BloodGlucose + "("
                + KEY_BloodGlucoseID + " integer primary key autoincrement, "
                + KEY_BloodGlucoseValue + " real, "
                + KEY_BloodGlucoseDate +" integer);";

        String create_Exerxise_table = "create table " + TABLE_Exercise + "("
                + KEY_ExerciseID + " integer primary key autoincrement, "
                + KEY_ExerciseDescription+ " text not null, "
                + KEY_ExerciseDate+" integer);";

        String create_Regimen_table = "create table " + TABLE_Regimen + "("
                + KEY_RegimenID + " integer primary key autoincrement, "
                + KEY_RegimenDescription+ " text not null, ";

        db.execSQL(TABLE_Regimen);
        db.execSQL(TABLE_BloodGlucose);
        db.execSQL(TABLE_Exercise);
        db.execSQL(TABLE_Medicine);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BloodGlucose);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Exercise);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Medicine);
        onCreate(db);


    }

    //to add BloodGlucose object to TABLE_BloodGlucose
    public void addBloogGlucose(BloodGlucose BGL) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_BloodGlucoseValue, BGL.getValue());//value
        values.put(KEY_BloodGlucoseDate, BGL.getDate().getTimeInMillis()); //date in milliseconds

        db.insert(TABLE_BloodGlucose, null, values);//Inserting Row
        db.close(); // Closing database connection
    }

    //to add Exercise object to TABLE_Exercise
    public void addExercise(Exercise ex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ExerciseDescription, ex.getDescription()); //Description
        values.put(KEY_ExerciseDate, ex.getDate().getTimeInMillis());////date in milliseconds

        db.insert(TABLE_Exercise, null, values);//Inserting Row
        db.close(); // Closing database connection
    }

    public void addMedicine(Medicine med) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_MedicineDescription , med.getDescription()); //Description
        values.put(KEY_MedicineDate , med.getDate().getTimeInMillis());////date in milliseconds

        db.insert(TABLE_Medicine, null, values);//Inserting Row
        db.close(); // Closing database connection
    }


    public void addRgeimen(Regimen newRegimen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_RegimenID, newRegimen.getDescription());

        db.insert(TABLE_Regimen, null, values);
        db.close();
    }

    public void deleteBloodGlucose(String id){//Deleting by KEY_id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BloodGlucose, KEY_BloodGlucoseID+ "=" + id , null);

        db.close();
    }

    public void deleteExercise(String x){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Exercise, KEY_ExerciseID+ "=" + x , null);

        db.close();
    }

    public void deleteMedicine(String x){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Medicine, KEY_MedicineID+ "=" + x , null);

        db.close();
    }

    public void deleteRegimen(String x){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Regimen, KEY_RegimenID+ "=" + x , null);

        db.close();
    }

    public ArrayList<Exercise> getAllExercises(){
        ArrayList<Exercise> output = new ArrayList<Exercise>();//the holder
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Exercise;
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