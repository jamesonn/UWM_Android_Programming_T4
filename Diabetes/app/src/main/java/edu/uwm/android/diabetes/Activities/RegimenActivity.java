package edu.uwm.android.diabetes.Activities;

        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.app.TimePickerDialog;
        import android.content.Context;
        import android.content.SharedPreferences;
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

        import java.sql.SQLOutput;
        import java.util.Calendar;

        import edu.uwm.android.diabetes.Database.DatabaseHandler;
        import edu.uwm.android.diabetes.Database.Medicine;
        import edu.uwm.android.diabetes.Database.Regimen;
        import edu.uwm.android.diabetes.R;

public class RegimenActivity extends AppCompatActivity {

    Button addRegimen, updateRegimen;
    DatabaseHandler databaseHandler;
    EditText regimenDiet, regimenDate, regimenTime, regimenExercise;
    Calendar calendar;
    int day, month, year;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regimen);
        databaseHandler = new DatabaseHandler(this);
        regimenDiet = (EditText) findViewById(R.id.regimenDiet);
        addRegimen = (Button) findViewById(R.id.addRegimen);
        updateRegimen = (Button) findViewById(R.id.updateRegimen);
        regimenDate = (EditText) findViewById(R.id.regimenDate);
        regimenTime = (EditText) findViewById(R.id.regimenTime);
        regimenExercise = (EditText) findViewById(R.id.exerciseEditText);
        addRegimen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Regimen regimen = new Regimen();
                regimen.setDietDescription(regimenDiet.getText().toString());
                regimen.setExerciseDescription(regimenExercise.getText().toString());
                regimen.setDate(regimenDate.getText().toString() + " " + regimenTime.getText().toString() );
                userName =  getIntent().getStringExtra("userName");
                databaseHandler.add(regimen, userName);
                Toast.makeText(RegimenActivity.this, regimenDiet.getText().toString() + " Added!",
                        Toast.LENGTH_LONG).show();
            }
        });


        if(getIntent().getIntExtra("regimenId",-1) == -1){
            updateRegimen.setVisibility(View.INVISIBLE); //Not coming from the List
        }else{
            //Coming from the list
            addRegimen.setVisibility(View.INVISIBLE);
            regimenDiet.setText(getIntent().getStringExtra("regimenDescription"));
            String dateAndTime = getIntent().getStringExtra("regimenDate");
            regimenDate.setText(dateAndTime.substring(0,8));
            regimenTime.setText(dateAndTime.substring(9,14));

        }


        updateRegimen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regimen regimen = new Regimen();
                regimen.setDietDescription(regimenDiet.getText().toString());
                regimen.setDate(regimenDate.getText().toString());
                int id = getIntent().getIntExtra("regimenId",-1);
                if(id != -1) {
                    databaseHandler.update(getIntent().getIntExtra("regimenId", -1), regimen,getIntent().getStringExtra("userName"));
                    getIntent().removeExtra("regimenDate");
                    getIntent().removeExtra("regimenDescription");
                    getIntent().removeExtra("regimenId");
                    Toast.makeText(RegimenActivity.this, "Regimen was updated", Toast.LENGTH_LONG).show();
                    regimenDate.getText().clear();
                    regimenDiet.getText().clear();
                }else{
                    Toast.makeText(RegimenActivity.this, "Can't Update now", Toast.LENGTH_LONG).show();
                }
                SharedPreferences sp = getSharedPreferences("medicineInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
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
                        String selectedHourString, selectedMinuteString;
                        selectedHourString = Integer.toString(selectedHour);
                        selectedMinuteString = Integer.toString(selectedMinute);
                        if(selectedHour<10){
                            selectedHourString = "0"+Integer.toString(selectedHour);
                        }
                        if(selectedMinute<10){
                            selectedMinuteString = "0"+Integer.toString(selectedMinute);
                        }
                        regimenTime.setText( selectedHourString + ":" + selectedMinuteString);
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
