package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class BGLActivity extends AppCompatActivity {
    Button addBGL, updateBGL;
    DatabaseHandler databaseHandler;
    EditText BGLDate, BGLTime;
    AutoCompleteTextView bglValue;
    Calendar calendar;
    int day, month, year, minute, hour;
    String userName;
    ArrayList<String> allBgl;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgl);
        databaseHandler = new DatabaseHandler(this);
        bglValue = (AutoCompleteTextView) findViewById(R.id.editTextBGLValue);
        BGLDate = (EditText) findViewById(R.id.editTextBGLDate);
        BGLTime = (EditText) findViewById(R.id.bglTime);
        addBGL = (Button) findViewById(R.id.addBGL);
        updateBGL = (Button) findViewById(R.id.updateBglData);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        BGLDate.setText(month+1  + "/" + day+ "/" + year);
        if (minute<10){
            BGLTime.setText(hour + ":0" + minute);
        }else{
            BGLTime.setText(hour + ":" + minute);
        }
        showSharedPreferences();

        //for auto complete
        allBgl =autoCom();


        if(getIntent().getIntExtra("bglId",-1) == -1){
            updateBGL.setEnabled(false);
        }else{
            addBGL.setEnabled(false);
        }

        if(getIntent().getIntExtra("bglId",-1) == -1){
            updateBGL.setVisibility(View.INVISIBLE);
        }else{
            addBGL.setVisibility(View.INVISIBLE);
            bglValue.setText(getIntent().getStringExtra("bglValue"));
            String dateAndTime = getIntent().getStringExtra("bglDate");
            BGLDate.setText(dateAndTime.substring(0,8));
            BGLTime.setText(dateAndTime.substring(9,14));
        }


        addBGL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add BGL button is called here.");
                if(!(Double.parseDouble(bglValue.getText().toString())<40.0 ||
                        Double.parseDouble(bglValue.getText().toString())>600.0)) {
                    if(!BGLDate.getText().toString().equals("")) {
                        BloodGlucose BGL = new BloodGlucose();
                        BGL.setValue(Double.parseDouble(bglValue.getText().toString()));
                        BGL.setDate(BGLDate.getText().toString() + " " + BGLTime.getText().toString());
                        userName = getIntent().getStringExtra("userName");
                        databaseHandler.add(BGL, userName);
                        Toast.makeText(BGLActivity.this, "Value " + bglValue.getText().toString() + " Date " +
                                        BGLDate.getText().toString() + " Added",
                                Toast.LENGTH_LONG).show();
                        BGLDate.getText().clear();
                        bglValue.getText().clear();
                        SharedPreferences sp = getSharedPreferences("bglInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        allBgl =autoCom();
                    } else if(bglValue.getText().toString().equals("")){
                        Toast.makeText(BGLActivity.this, "Please add a BGL value",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(BGLActivity.this, "Please add a date",
                                Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(BGLActivity.this, "The BGL value is outside the range (40-600)",
                        Toast.LENGTH_LONG).show();
                }

            }
        });

        BGLTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BGLActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        BGLTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        updateBGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Double.parseDouble(bglValue.getText().toString())<40.0 ||
                        Double.parseDouble(bglValue.getText().toString())>600.0)) {
                    BloodGlucose bgl = new BloodGlucose();
                    bgl.setValue(Double.parseDouble(bglValue.getText().toString()));
                    bgl.setDate(BGLDate.getText().toString() + " " + BGLTime.getText().toString());
                    int id = getIntent().getIntExtra("bglId", -1);
                    if (id != -1) {
                        databaseHandler.update(getIntent().getIntExtra("bglId", -1), bgl, getIntent().getStringExtra("userName"));
                        getIntent().removeExtra("bglDate");
                        getIntent().removeExtra("bglValue");
                        getIntent().removeExtra("bglId");
                        Toast.makeText(BGLActivity.this, "BGL was updated", Toast.LENGTH_LONG).show();
                        BGLDate.getText().clear();
                        bglValue.getText().clear();
                        allBgl =autoCom();
                    } else {
                        Toast.makeText(BGLActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                    }
                    SharedPreferences sp = getSharedPreferences("bglInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                } else{Toast.makeText(BGLActivity.this, "The BGL value is outside the range (40-600)",
                        Toast.LENGTH_LONG).show();}
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


                BGLDate.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void saveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences("bglInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("value", bglValue.getText().toString());
        editor.putString("date",BGLDate.getText().toString());
        editor.commit();
    }

    //use this inside onCreate()
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("bglInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            bglValue.setText(sp.getString("value", ""));
            BGLDate.setText(sp.getString("date", ""));
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
        saveSharedPreferences();
        }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private ArrayList<String> autoCom(){
        ArrayList<String> ret =new ArrayList<>();

        Cursor cursor = databaseHandler.getData(new BloodGlucose());
        if(cursor.moveToFirst()){
            do{
                if(!ret.contains(cursor.getString(2)))
                    ret.add(cursor.getString(2));

            }while(cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,ret);
        bglValue.setAdapter(adapter);
        cursor.close();
        return ret;
    }
}
