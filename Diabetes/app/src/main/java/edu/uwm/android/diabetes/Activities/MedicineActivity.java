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

import java.util.Calendar;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Medicine;
import edu.uwm.android.diabetes.R;

public class MedicineActivity extends AppCompatActivity {

    Button addMedicine, updateMedicine;
    DatabaseHandler databaseHandler;
    EditText medicineDescription, medicineDate;
    Calendar calendar;
    int day, month, year;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        databaseHandler = new DatabaseHandler(this);
        medicineDescription = (EditText) findViewById(R.id.editTextMedicineDescription);
        medicineDate = (EditText) findViewById(R.id.editTextMedicineDate);
        addMedicine = (Button) findViewById(R.id.addMedicine);
        updateMedicine = (Button) findViewById(R.id.updateMedincineData);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        medicineDate.setText(month+1  + "/" + day+ "/" + year);

        addMedicine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Exercise button is called here.");
                Medicine medicine = new Medicine();
                medicine.setDescription(medicineDescription.getText().toString());
                medicine.setDate(medicineDate.getText().toString());
                userName =  getIntent().getStringExtra("userName");
                databaseHandler.add(medicine, userName);
                Toast.makeText(MedicineActivity.this, "Description "+ medicineDescription.getText().toString() + " Date "+
                                medicineDate.getText().toString()+" Added",
                        Toast.LENGTH_LONG).show();
                medicineDate.getText().clear();
                medicineDescription.getText().clear();

            }
        });
        updateMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicine medicine = new Medicine();
                medicine.setDescription(medicineDescription.getText().toString());
                medicine.setDate(medicineDate.getText().toString());
                int id =getIntent().getIntExtra("medicineId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("medicineId", -1), medicine,getIntent().getStringExtra("userName"));
                    getIntent().removeExtra("medicineDate");
                    getIntent().removeExtra("medicineDescription");
                    getIntent().removeExtra("medicineId");
                    Toast.makeText(MedicineActivity.this, "Medicine was updated", Toast.LENGTH_LONG).show();
                    medicineDate.getText().clear();
                    medicineDescription.getText().clear();
                }else{
                    Toast.makeText(MedicineActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }
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
                medicineDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);

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
