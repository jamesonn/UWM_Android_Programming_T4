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

import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class BGLActivity extends AppCompatActivity {
    Button addBGL, showBGL;
    DatabaseHandler databaseHandler;
    EditText BGLDescription, BGLDate, BGLCalories;
    Calendar calendar;
    int day, month, year;
    String userName;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgl);
        databaseHandler = new DatabaseHandler(this);
        BGLDescription = (EditText) findViewById(R.id.editTextBGLDescription);
        BGLDate = (EditText) findViewById(R.id.editTextBGLDate);
        addBGL = (Button) findViewById(R.id.addBGL);
        showBGL = (Button) findViewById(R.id.showBglData);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        BGLDate.setText(month+1  + "/" + day+ "/" + year);

        addBGL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add BGL button is called here.");
                BloodGlucose BGL = new BloodGlucose();
                BGL.setValue(Double.parseDouble(BGLDescription.getText().toString()));
                BGL.setDate(BGLDate.getText().toString());
                userName =  getIntent().getStringExtra("userName");

                databaseHandler.add(BGL,userName);
                Toast.makeText(BGLActivity.this, "Description "+ BGLDescription.getText().toString() + " Date "+
                                BGLDate.getText().toString()+" Added",
                        Toast.LENGTH_LONG).show();
                BGLDate.getText().clear();
                BGLDescription.getText().clear();

            }
        });

        showBGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloodGlucose BGL = new BloodGlucose();
                Cursor cursor = databaseHandler.getDatabyUserName(BGL, userName);
                if (cursor.getCount() == 0) {
                    Toast.makeText(BGLActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
                        stringBuffer.append("User  " + cursor.getString(1) + "\n");
                        stringBuffer.append("Description  " + cursor.getString(2) + "\n");
                        stringBuffer.append("Date " +cursor.getString(3) + "\n");
                        stringBuffer.append("---------------------\n");
                    }
                    Toast.makeText(BGLActivity.this, stringBuffer.toString(), Toast.LENGTH_LONG).show();

                }
            }
        });

        BGLDate.setOnClickListener(new View.OnClickListener() {
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


                BGLDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

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
