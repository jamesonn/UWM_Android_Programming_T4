package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class DActivity extends AppCompatActivity {

    Button addDiet, showDiet;
    DatabaseHandler databaseHandler;
    EditText dietDescription, dietDate;
    Calendar calendar;
    int day, month, year;
    String userName;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        databaseHandler = new DatabaseHandler(this);
        dietDescription= (EditText) findViewById(R.id.editTextDietDescription);
        dietDate= (EditText) findViewById(R.id.dietDate);
        addDiet = (Button) findViewById(R.id.addDiet);
        showDiet = (Button) findViewById(R.id.showDietData);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dietDate.setText(month+1  + "/" + day+ "/" + year);

        addDiet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add BGL button is called here.");
                Diet diet = new Diet();
                diet.setDescription(dietDescription.getText().toString());
                diet.setDate(dietDate.getText().toString());
                userName =  getIntent().getStringExtra("userName");

                databaseHandler.add(diet,userName);
                Toast.makeText(DActivity.this, "Description "+ dietDescription.getText().toString() + " Date "+
                                dietDate.getText().toString()+" Added",
                        Toast.LENGTH_LONG).show();
                dietDate.getText().clear();
                dietDescription.getText().clear();

            }
        });

        showDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diet diet = new Diet();
                Cursor cursor = databaseHandler.getDatabyUserName(diet, userName);
                if (cursor.getCount() == 0) {
                    Toast.makeText(DActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
                        stringBuffer.append("User  " + cursor.getString(1) + "\n");
                        stringBuffer.append("Description  " + cursor.getString(2) + "\n");
                        stringBuffer.append("Date " +cursor.getString(3) + "\n");
                        stringBuffer.append("---------------------\n");
                    }
                    Toast.makeText(DActivity.this, stringBuffer.toString(), Toast.LENGTH_LONG).show();

                }
            }
        });

        dietDate.setOnClickListener(new View.OnClickListener() {
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
