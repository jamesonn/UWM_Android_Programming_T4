package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class DietActivity extends AppCompatActivity {

    Button addDiet, showDiet;
    DatabaseHandler databaseHandler;
    EditText dietCalories, dietDate, dietTime;
    Calendar calendar;
    int day, month, year;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        databaseHandler = new DatabaseHandler(this);
        dietCalories = (EditText) findViewById(R.id.editTextdietCalories);
        addDiet = (Button) findViewById(R.id.addDiet);
        showDiet = (Button) findViewById(R.id.showDietData);

        addDiet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Regimen button is called here.");
                Diet diet = new Diet();
                diet.setDescription(dietCalories.getText().toString());
                diet.setDate(dietDate.getText().toString() + " " + dietTime.getText().toString() );
                userName =  getIntent().getStringExtra("userName");
                databaseHandler.add(diet, userName);
                Toast.makeText(DietActivity.this, dietCalories.getText().toString() + " Added!",
                        Toast.LENGTH_LONG).show();
            }
        });

        showDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diet diet = new Diet();
                Cursor cursor = databaseHandler.getDatabyUserName(diet, userName);
                if (cursor.getCount() == 0) {
                    Toast.makeText(DietActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
                        stringBuffer.append("User  " + cursor.getString(1) + "\n");
                        stringBuffer.append("Description  " + cursor.getString(2) + "\n");
                    }
                    Toast.makeText(DietActivity.this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dietDate = (EditText) findViewById(R.id.dietDate);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dietDate.setText(month+1  + "/" + day+ "/" + year);
        dietTime = (EditText) findViewById(R.id.dietTime);

        dietDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
        dietTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DietActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dietTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                 }
        });

    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dietDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}

