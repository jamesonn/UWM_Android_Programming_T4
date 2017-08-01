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

    ImageButton homeButton;
    Button addDiet, showDiet;
    DatabaseHandler databaseHandler;
    EditText dietDescription, dietDate, dietCalories;
    Calendar calendar;
    int day, month, year;
    String userName;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        databaseHandler = new DatabaseHandler(this);
        dietDescription = (EditText) findViewById(R.id.editTextDietDescription);
        dietDate = (EditText) findViewById(R.id.dietDate);
        addDiet = (Button) findViewById(R.id.addDiet);
        showDiet = (Button) findViewById(R.id.showDietData);
        homeButton = (ImageButton) findViewById(R.id.dietHomeButton);
        dietDate = (EditText) findViewById(R.id.dietDate);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dietDate.setText(month+1  + "/" + day+ "/" + year);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        addDiet.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                System.out.println("The add Diet button is called here.");
//                Diet Diet = new Diet();
//                Diet.setDescription(DietDescription.getText().toString());
//                Diet.setDate(DietDate.getText().toString());
//                userName =  getIntent().getStringExtra("userName");
//
//                databaseHandler.add(Diet,userName);
//                Toast.makeText(DietActivity.this, "Description "+ DietDescription.getText().toString() + " Date "+
//                                DietDate.getText().toString()+" Added",
//                        Toast.LENGTH_LONG).show();
//                DietDate.getText().clear();
//                DietDescription.getText().clear();
//
//            }
//        });

//        showDiet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Diet Diet = new Diet();
//                Cursor cursor = databaseHandler.getDatabyUserName(Diet, userName);
//                if (cursor.getCount() == 0) {
//                    Toast.makeText(DietActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
//                } else {
//                    StringBuffer stringBuffer = new StringBuffer();
//                    while (cursor.moveToNext()) {
//                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
//                        stringBuffer.append("User  " + cursor.getString(1) + "\n");
//                        stringBuffer.append("Description  " + cursor.getString(2) + "\n");
//                        stringBuffer.append("Date " +cursor.getString(3) + "\n");
//                        stringBuffer.append("---------------------\n");
//                    }
//                    Toast.makeText(DietActivity.this, stringBuffer.toString(), Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
//
//        DietDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DateDialog();
//            }
//        });
//    }
//
//    public void DateDialog() {
//        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//
//                DietDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
//
//            }
//        };
//
//        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
//        dpDialog.show();
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
