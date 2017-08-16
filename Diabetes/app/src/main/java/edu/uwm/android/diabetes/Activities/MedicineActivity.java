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
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Medicine;
import edu.uwm.android.diabetes.R;

public class MedicineActivity extends AppCompatActivity {

    Button addMedicine, updateMedicine;
    DatabaseHandler databaseHandler;
    EditText medicineDate, medicineTime;
    AutoCompleteTextView medicineDescription;
    Calendar calendar;
    int day, month, year, hour, minute;
    String userName;
    ArrayList<String> allMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        databaseHandler = new DatabaseHandler(this);
        medicineDescription = (AutoCompleteTextView) findViewById(R.id.editTextMedicineDescription);
        medicineDate = (EditText) findViewById(R.id.editTextMedicineDate);
        medicineTime = (EditText) findViewById(R.id.medicineTime);
        addMedicine = (Button) findViewById(R.id.addMedicine);
        updateMedicine = (Button) findViewById(R.id.updateMedincineData);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (minute<10){
            medicineTime.setText(hour + ":0" + minute);
        }else{
            medicineTime.setText(hour + ":" + minute);
        }
        medicineDate.setText(month+1  + "/" + day+ "/" + year);

        showSharedPreferences();

        //for auto complete
        allMedicine =autoCom();



        medicineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MedicineActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        medicineTime.setText( selectedHourString + ":" + selectedMinuteString);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        if(getIntent().getIntExtra("medicineId",-1) == -1){
            updateMedicine.setEnabled(false);
            System.out.println("Update is invisible");
        }else{
            addMedicine.setEnabled(false);
            medicineDescription.setText(getIntent().getStringExtra("medicineDescription"));
            String dateAndTime = getIntent().getStringExtra("medicineDate");
            medicineDate.setText(dateAndTime.substring(0,8));
            medicineTime.setText(dateAndTime.substring(9,14));
        }

        addMedicine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(medicineDate.getText().toString().equals("")){
                    Toast.makeText(MedicineActivity.this, "Please add a date",
                            Toast.LENGTH_LONG).show();
                }else if(medicineDescription.getText().toString().equals("")){
                    Toast.makeText(MedicineActivity.this, "Please add a description",
                            Toast.LENGTH_LONG).show();
                }else if(medicineTime.getText().toString().equals("")){
                    Toast.makeText(MedicineActivity.this, "Please add a time",
                            Toast.LENGTH_LONG).show();
                }else {
                    Medicine medicine = new Medicine();
                    medicine.setDescription(medicineDescription.getText().toString());
                    medicine.setDate(medicineDate.getText().toString() + " " + medicineTime.getText().toString());
                    userName = getIntent().getStringExtra("userName");
                    databaseHandler.add(medicine, userName);
                    allMedicine =autoCom();
                    Toast.makeText(MedicineActivity.this, "Description " + medicineDescription.getText().toString() + " Date " +
                                    medicineDate.getText().toString() + " Added",
                            Toast.LENGTH_LONG).show();
                    medicineDate.getText().clear();
                    medicineDescription.getText().clear();
                    SharedPreferences sp = getSharedPreferences("medicineInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                }
            }
        });
        updateMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicine medicine = new Medicine();
                medicine.setDescription(medicineDescription.getText().toString());
                medicine.setDate(medicineDate.getText().toString()+" "+ medicineTime.getText().toString());
                int id =getIntent().getIntExtra("medicineId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("medicineId", -1), medicine,getIntent().getStringExtra("userName"));
                    allMedicine =autoCom();
                    getIntent().removeExtra("medicineDate");
                    getIntent().removeExtra("medicineDescription");
                    getIntent().removeExtra("medicineId");
                    Toast.makeText(MedicineActivity.this, "Medicine was updated", Toast.LENGTH_LONG).show();
                    medicineDate.getText().clear();
                    medicineDescription.getText().clear();
                }else{
                    Toast.makeText(MedicineActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }
                SharedPreferences sp = getSharedPreferences("medicineInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
            }
        });
        medicineDate.setOnClickListener(new View.OnClickListener() {
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
                medicineDate.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void saveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences("medicineInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("description", medicineDescription.getText().toString());
        editor.putString("date",medicineDate.getText().toString());
        editor.commit();
    }

    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("medicineInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            medicineDescription.setText(sp.getString("description", ""));
            medicineDate.setText(sp.getString("date", ""));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHandler.close();
    }

    private ArrayList<String> autoCom(){
        ArrayList<String> ret =new ArrayList<>();

        Cursor cursor = databaseHandler.getData(new Medicine());
        if(cursor.moveToFirst()){
            do{
                if(!ret.contains(cursor.getString(2)))
                    ret.add(cursor.getString(2));

            }while(cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,ret);
        medicineDescription.setAdapter(adapter);
        cursor.close();
        return ret;
    }
}
