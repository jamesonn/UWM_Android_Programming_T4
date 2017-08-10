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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class DietActivity extends AppCompatActivity {

    Button addDiet, updatediet;
    DatabaseHandler databaseHandler;
    AutoCompleteTextView dietDescription;
    EditText dietDate, dietTime;
    Calendar calendar;
    int day, month, year, hour, minute;
    String userName;
    ArrayList<String> allDiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        databaseHandler = new DatabaseHandler(this);
        dietDescription = (AutoCompleteTextView) findViewById(R.id.editTextDietDescription);
        addDiet = (Button) findViewById(R.id.addDiet);
        updatediet = (Button) findViewById(R.id.updateDiet);
        dietDate = (EditText) findViewById(R.id.dietDate);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        dietDate.setText(month+1  + "/" + day+ "/" + year);
        dietTime = (EditText) findViewById(R.id.dietTime);
        if (minute<10){
            dietTime.setText(hour + ":0" + minute);
        }else{
            dietTime.setText(hour + ":" + minute);
        }


        //for auto complete
        allDiet =new ArrayList<>();
        Cursor cursor = databaseHandler.getData(new Diet());
        if(cursor.moveToFirst()){
            do{
                if(!allDiet.contains(cursor.getString(2)))
               allDiet.add(cursor.getString(2));

            }while(cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,allDiet);
        dietDescription.setAdapter(adapter);



        if(getIntent().getIntExtra("dietId",-1) == -1){
            updatediet.setVisibility(View.INVISIBLE);
        }else{
            addDiet.setVisibility(View.INVISIBLE);
            dietDescription.setText(getIntent().getStringExtra("exerciseDescription"));
            String dateAndTime = getIntent().getStringExtra("dietDate");
            dietDate.setText(dateAndTime.substring(0,8));
            dietTime.setText(dateAndTime.substring(9,14));
        }

        addDiet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Regimen button is called here.");
                Diet diet = new Diet();
                diet.setDescription(dietDescription.getText().toString());
                diet.setDate(dietDate.getText().toString() + " " + dietTime.getText().toString() );
                userName =  getIntent().getStringExtra("userName");
                databaseHandler.add(diet, userName);
                Toast.makeText(DietActivity.this, dietDescription.getText().toString() + " Added!",
                        Toast.LENGTH_LONG).show();
                dietDate.getText().clear();
                dietDescription.getText().clear();
            }
        });

        updatediet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diet diet = new Diet();
                diet.setDescription(dietDescription.getText().toString());
                diet.setDate(dietDate.getText().toString() +" " + dietTime.getText().toString());
                int id =getIntent().getIntExtra("dietId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("dietId", -1), diet,getIntent().getStringExtra("userName"));
                    getIntent().removeExtra("dietDate");
                    getIntent().removeExtra("dietDescription");
                    getIntent().removeExtra("dietId");
                    Toast.makeText(DietActivity.this, "Diet was updated", Toast.LENGTH_LONG).show();
                    dietDate.getText().clear();
                    dietDescription.getText().clear();
                }else{
                    Toast.makeText(DietActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }

                SharedPreferences sp = getSharedPreferences("exerciseInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
            }
        });




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
                dietDate.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);
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

