package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class ExerciseActivity extends AppCompatActivity {
    Button addExercise, updateExercise;
    DatabaseHandler databaseHandler;
    EditText exerciseDate, exerciseTime;
    AutoCompleteTextView exerciseDescription;
    Calendar calendar;
    int day, month, year, minute, hour;
    String userName;
    ArrayList<String> allExercise;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        databaseHandler = new DatabaseHandler(this);
        exerciseDescription = (AutoCompleteTextView) findViewById(R.id.editTextExerciseDescription);
        exerciseDate = (EditText) findViewById(R.id.exerciseDate);
        addExercise = (Button) findViewById(R.id.addExercise);
        updateExercise = (Button) findViewById(R.id.updateExerciseData);

        exerciseDate = (EditText) findViewById(R.id.exerciseDate);
        exerciseTime = (EditText) findViewById(R.id.exerciseTime);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        exerciseDate.setText(month+1  + "/" + day+ "/" + year);
        if (minute<10){
            exerciseTime.setText(hour + ":0" + minute);
        }else{
            exerciseTime.setText(hour + ":" + minute);
        }
        showSharedPreferences();

        //for auto complete
        allExercise =autoCom();


        exerciseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ExerciseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedHourString, selectedMinuteString;
                        selectedHourString = Integer.toString(selectedHour);
                        selectedMinuteString = Integer.toString(selectedMinute);
                        if(selectedHour<10){
                            selectedHourString = "0"+Integer.toString(selectedHour);
                        }
                        if(selectedMinute<10){
                            selectedMinuteString = "0"+Integer.toString(selectedMinute);
                        }
                        exerciseTime.setText( selectedHourString + ":" + selectedMinuteString);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        if(getIntent().getIntExtra("exerciseId",-1) == -1){
            updateExercise.setEnabled(false);
        }else{
            addExercise.setEnabled(false);
            exerciseDescription.setText(getIntent().getStringExtra("exerciseDescription"));
            String dateAndTime = getIntent().getStringExtra("exerciseDate");
            exerciseDate.setText(dateAndTime.substring(0,8));
            exerciseTime.setText(dateAndTime.substring(9,14));
        }

        addExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(exerciseDescription.getText().toString().equals("")){
                    Toast.makeText(ExerciseActivity.this, "Please add a description",
                            Toast.LENGTH_LONG).show();
                }else if(exerciseDate.getText().toString().equals("")){
                    Toast.makeText(ExerciseActivity.this, "Please add a date",
                            Toast.LENGTH_LONG).show();
                }else if(exerciseTime.getText().toString().equals("")){
                    Toast.makeText(ExerciseActivity.this, "Please add a time",
                            Toast.LENGTH_LONG).show();
                }else {
                    Exercise exercise = new Exercise();
                    exercise.setDescription(exerciseDescription.getText().toString());
                    exercise.setDate(exerciseDate.getText().toString() + " " + exerciseTime.getText().toString());
                    userName = getIntent().getStringExtra("userName");
                    databaseHandler.add(exercise, userName);
                    allExercise =autoCom();
                    Toast.makeText(ExerciseActivity.this, "Description " + exerciseDescription.getText().toString() + " Date " +
                                    exerciseDate.getText().toString() + " Added",
                            Toast.LENGTH_LONG).show();
                    exerciseDate.getText().clear();
                    exerciseDescription.getText().clear();
                    SharedPreferences sp = getSharedPreferences("exerciseInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                }
            }
        });

        updateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                exercise.setDescription(exerciseDescription.getText().toString());
                exercise.setDate(exerciseDate.getText().toString() +" " + exerciseTime.getText().toString());
                int id =getIntent().getIntExtra("exerciseId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("exerciseId", -1), exercise,getIntent().getStringExtra("userName"));
                    allExercise =autoCom();
                    getIntent().removeExtra("exerciseDate");
                    getIntent().removeExtra("exerciseDescription");
                    getIntent().removeExtra("exerciseId");
                    Toast.makeText(ExerciseActivity.this, "Exercise was updated", Toast.LENGTH_LONG).show();
                    exerciseDate.getText().clear();
                    exerciseDescription.getText().clear();
                }else{
                    Toast.makeText(ExerciseActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }

                SharedPreferences sp = getSharedPreferences("exerciseInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
            }
        });

        exerciseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                  exerciseDate.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);
             }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void saveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences("exerciseInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("description", exerciseDescription.getText().toString());
        editor.putString("date",exerciseDate.getText().toString());
        editor.commit();
    }

    //use this inside onCreate()
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("exerciseInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            exerciseDescription.setText(sp.getString("description", ""));
            exerciseDate.setText(sp.getString("date", ""));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPreferences();}

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {super.onRestart();}

    private ArrayList<String> autoCom(){
        ArrayList<String> ret =new ArrayList<>();

        Cursor cursor = databaseHandler.getData(new Exercise());
        if(cursor.moveToFirst()){
            do{
                if(!ret.contains(cursor.getString(2)))
                    ret.add(cursor.getString(2));

            }while(cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,ret);
        exerciseDescription.setAdapter(adapter);
        cursor.close();
        return ret;
    }
}
