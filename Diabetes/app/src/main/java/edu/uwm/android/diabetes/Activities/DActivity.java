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

    Button addDiet, updateDietData;
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
        updateDietData = (Button) findViewById(R.id.updateDietData);

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

        updateDietData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diet diet = new Diet();
                diet.setDescription(dietDescription.getText().toString());
                diet.setDate(dietDate.getText().toString());
                int id =getIntent().getIntExtra("dietId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("dietId", -1), diet,getIntent().getStringExtra("userName"));
                    getIntent().removeExtra("dietDate");
                    getIntent().removeExtra("dietDescription");
                    getIntent().removeExtra("dietId");
                    Toast.makeText(DActivity.this, "Diet was updated", Toast.LENGTH_LONG).show();
                    dietDate.getText().clear();
                    dietDescription.getText().clear();
                }else{
                    Toast.makeText(DActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
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
