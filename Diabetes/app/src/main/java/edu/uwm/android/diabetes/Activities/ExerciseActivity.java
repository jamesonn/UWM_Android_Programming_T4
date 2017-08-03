package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class ExerciseActivity extends AppCompatActivity {
    Button addExercise, updateExercise;
    DatabaseHandler databaseHandler;
    EditText exerciseDescription, exerciseDate, exerciseCalories;
    Calendar calendar;
    int day, month, year;
    String userName;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        databaseHandler = new DatabaseHandler(this);
        exerciseCalories = (EditText) findViewById(R.id.editTextExerciseCalories);
        exerciseDescription = (EditText) findViewById(R.id.editTextExerciseDescription);
        exerciseDate = (EditText) findViewById(R.id.exerciseDate);
        addExercise = (Button) findViewById(R.id.addExercise);
        updateExercise = (Button) findViewById(R.id.updateExerciseData);

        exerciseDate = (EditText) findViewById(R.id.exerciseDate);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        exerciseDate.setText(month+1  + "/" + day+ "/" + year);

//        if(!(getIntent().getStringExtra("id").equals(null) && getIntent().getStringExtra("description").equals(null)
//                && getIntent().getStringExtra("date").equals(null)) ){

//        }

        addExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Exercise button is called here.");
                Exercise exercise = new Exercise();
                exercise.setDescription(exerciseDescription.getText().toString());
                exercise.setDate(exerciseDate.getText().toString());
                userName =  getIntent().getStringExtra("userName");

                databaseHandler.add(exercise,userName);
                Toast.makeText(ExerciseActivity.this, "Description "+ exerciseDescription.getText().toString() + " Date "+
                                exerciseDate.getText().toString()+" Added",
                        Toast.LENGTH_LONG).show();
                exerciseDate.getText().clear();
                exerciseDescription.getText().clear();

            }
        });

        updateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                exercise.setDescription(exerciseDescription.getText().toString());
                exercise.setDate(exerciseDate.getText().toString());
                int id =getIntent().getIntExtra("exerciseId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("exerciseId", -1), exercise,getIntent().getStringExtra("userName"));
                    getIntent().removeExtra("exerciseDate");
                    getIntent().removeExtra("exerciseDescription");
                    getIntent().removeExtra("exerciseId");
                    Toast.makeText(ExerciseActivity.this, "Exercise was updated", Toast.LENGTH_LONG).show();
                    exerciseDate.getText().clear();
                    exerciseDescription.getText().clear();
                }else{
                    Toast.makeText(ExerciseActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }
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


                exerciseDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
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
    protected void onPause() {super.onPause();}

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {super.onRestart();}
}
