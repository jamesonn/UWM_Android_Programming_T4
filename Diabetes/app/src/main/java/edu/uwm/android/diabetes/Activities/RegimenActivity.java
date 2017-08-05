package edu.uwm.android.diabetes.Activities;

        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.app.TimePickerDialog;
        import android.database.Cursor;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.format.DateFormat;
        import android.view.View;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import java.util.Calendar;

        import edu.uwm.android.diabetes.Database.DatabaseHandler;
        import edu.uwm.android.diabetes.Database.Regimen;
        import edu.uwm.android.diabetes.R;

public class RegimenActivity extends AppCompatActivity {

    Button addRegimen, showRegimen;
    DatabaseHandler databaseHandler;
    EditText foodDescription, regimenDate, regimenTime;
    Calendar calendar;
    int day, month, year;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regimen);
        databaseHandler = new DatabaseHandler(this);
        foodDescription = (EditText) findViewById(R.id.editText_foodDescription);
        addRegimen = (Button) findViewById(R.id.addRegimen);
        showRegimen = (Button) findViewById(R.id.showData);
        addRegimen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("The add Regimen button is called here.");
                Regimen regimen = new Regimen();
                regimen.setDescription(foodDescription.getText().toString());
                regimen.setDate(regimenDate.getText().toString() + " " + regimenTime.getText().toString() );
                userName =  getIntent().getStringExtra("userName");
                databaseHandler.add(regimen, userName);
                Toast.makeText(RegimenActivity.this, foodDescription.getText().toString() + " Added!",
                        Toast.LENGTH_LONG).show();
            }
        });
       showRegimen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regimen regimen = new Regimen();
                Cursor cursor = databaseHandler.getDatabyUserName(regimen, userName);
                if (cursor.getCount() == 0) {
                    Toast.makeText(RegimenActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append("ID " + cursor.getString(0) + "\n");
                        stringBuffer.append("User  " + cursor.getString(1) + "\n");
                        stringBuffer.append("Description  " + cursor.getString(2) + "\n");
                    }
                    Toast.makeText(RegimenActivity.this, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        regimenDate = (EditText) findViewById(R.id.regimenDate);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        regimenDate.setText(month+1  + "/" + day+ "/" + year);
        regimenTime = (EditText) findViewById(R.id.regimenTime);

        regimenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
        regimenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RegimenActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        regimenTime.setText( selectedHour + ":" + selectedMinute);
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
                 regimenDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
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
