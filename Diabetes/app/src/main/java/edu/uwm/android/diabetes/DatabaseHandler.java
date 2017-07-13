package edu.uwm.android.diabetes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "Diabetes";

    // We add the tables here
    private static final String TABLE_BloodGlucose = "Blood Glucose";
    private static final String TABLE_Exercise= "Exercise";

    // We add BloodGlucose Table Columns here
    private static final String KEY_BloodGlucoseID = "id";
    private static final String KEY_BloodGlucoseValue = "Value";
    private static final String KEY_BloodGlucoseDate = "Date";


    // We add Exercise Table Columns here
    private static final String KEY_ExerciseID = "id";
    private static final String KEY_ExerciseDescription = "Description";
    private static final String KEY_ExerciseDate = "Date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_BloodGlucose_table = "create table " + TABLE_BloodGlucose + "("
                + KEY_BloodGlucoseID + " integer primary key autoincrement, "
                + KEY_BloodGlucoseValue + " real, "
                + KEY_BloodGlucoseDate +" text not null);";

        String create_Exerxise_table = "create table " + TABLE_Exercise + "("
                + KEY_ExerciseID + " integer primary key autoincrement, "
                + KEY_ExerciseDescription+ " text not null, "
                + KEY_ExerciseDate+" text not null);";

        db.execSQL(TABLE_BloodGlucose);
        db.execSQL(TABLE_Exercise);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BloodGlucose);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Exercise);
        onCreate(db);


    }

    //Method to add BloogGlucose object to TABLE_BloogGlucose
    public void addBloogGlucose(BloodGlucose BG) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_BloodGlucoseValue, BG.getValue());//value
        values.put(KEY_BloodGlucoseDate, BG.getDate()); //date

        db.insert(TABLE_BloodGlucose, null, values);//Inserting Row
        db.close(); // Closing database connection
    }

    //Method to add Exercise object to TABLE_Exercise
    public void addExercise(Exercise ex) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ExerciseDescription, ex.getDescription()); //Description
        values.put(KEY_ExerciseDate, ex.getDate());//date

        db.insert(TABLE_Exercise, null, values);//Inserting Row
        db.close(); // Closing database connection
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


}