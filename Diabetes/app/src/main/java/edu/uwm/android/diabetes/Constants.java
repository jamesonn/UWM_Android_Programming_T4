package edu.uwm.android.diabetes;

public class Constants {
    public static final int DATABASE_VERSION = 1;

    //Database name
    public static final String DATABASE_NAME = "Diabetes";

    public static final String TABLE_BLOOD_GLUCOSE = "BloodGlucose";
    public static final String TABLE_EXERCISE= "Exercise";
    public static final String TABLE_DIET= "Diet";
    public static final String TABLE_REGIMEN = "Regimen";
    public static final String TABLE_MEDICINE= "Medicine";
    public static final String TABLE_LOGIN = "Login";


    public static final String USERNAME = "USERNAME";
    public static final String LOGIN_USERNAME = "USERNAME";
    public static final String LOGIN_PASSWORD = "PASSWORD";

    public static final String BLOOD_GLUCOSE_ID = "BG_ID";
    public static final String BLOOD_GLUCOSE_VALUE = "Value";
    public static final String BLOOD_GLUCOSE_DATE = "Date";

    public static final String EXERCISE_ID = "EXERCISE_ID";
    public static final String EXERCISE_DESCRIPTION = "Description";
    public static final String EXERCISE_DATE = "Date";

    public static final String DIET_ID = "DIET_ID";
    public static final String DIET_DESCRIPTION = "Description";
    public static final String DIET_DATE = "Date";

    public static final String MEDICINE_ID = "MEDICINE_ID";
    public static final String MEDICINE_DESCRIPTION = "Description";
    public static final String MEDICINE_DATE = "Date";

    public static final String REGIMEN_ID = "REGIMEN_ID";
    public static final String REGIMEN_DESCRIPTION = "Description";
    public static final String REGIMEN_DATE = "Date";

    public static final String REGIMEN_CLASS = "REGIMEN";
    public static final String DIET_CLASS = "DIET";
    public static final String MEDICINE_CLASS = "MEDICINE";
    public static final String BLOODGLUCOSE_CLASS = "BLOODGLUCOSE";
    public static final String EXERCISE_CLASS = "EXERCISE";
}
