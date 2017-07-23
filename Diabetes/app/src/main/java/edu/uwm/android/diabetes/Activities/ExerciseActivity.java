package edu.uwm.android.diabetes.Activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    ImageButton homeButton;
    Button addExercise, showExercise;
    DatabaseHandler databaseHandler;
    EditText exerciseDescription, exerciseDate, exerciseCalories;
    Calendar calendar;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        databaseHandler = new DatabaseHandler(this);
        exerciseCalories = (EditText) findViewById(R.id.editTextExerciseCalories);
        exerciseDescription = (EditText) findViewById(R.id.editTextExerciseDescription);
        exerciseDate = (EditText) findViewById(R.id.exerciseDate);
        addExercise = (Button) findViewById(R.id.addExercise);
        showExercise = (Button) findViewById(R.id.showExerciseData);
        homeButton = (ImageButton) findViewById(R.id.exerciseHomeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Exercise button is called here.");
                Exercise exercise = new Exercise();
                exercise.setDescription(exerciseDescription.getText().toString());

               /* Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
                try {
                    calendar.setTime(sdf.parse("07/22/2017"));
                } catch (ParseException e) {
                    System.out.println("This conversion did not work.");
                    e.printStackTrace();
                }
                exercise.setDate(calendar);
                */
                databaseHandler.add(exercise);
                Toast.makeText(ExerciseActivity.this, "Description "+ exerciseDescription.getText().toString() + " Added",
                        Toast.LENGTH_LONG).show();

            }
        });

        showExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exercise exercise = new Exercise();
                Cursor cursor = databaseHandler.getData(exercise);
                if (cursor.getCount() == 0) {
                    Toast.makeText(ExerciseActivity.this, "No data to show", Toast.LENGTH_LONG).show();
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
                        stringBuffer.append("Description  " + cursor.getString(1) + "\n");
                    }
                    Toast.makeText(ExerciseActivity.this, stringBuffer.toString(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
