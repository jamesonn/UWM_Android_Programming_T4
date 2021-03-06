package edu.uwm.android.diabetes.Activities;
import android.app.AlarmManager;
import android.app.DatePickerDialog;import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.R;

public class RegimenActivity extends AppCompatActivity {

    Button addRegimen, updateRegimen;
    DatabaseHandler databaseHandler;
    EditText regimenDiet, regimenDate, regimenTime, regimenExercise;
    Calendar calendar;
    int day, month, year;
    String userName;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        userName =  getIntent().getStringExtra("userName");
        try {
            sendNotification();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addRegimen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Regimen regimen = new Regimen();
                regimen.setDietDescription("Diet : "+ regimenDiet.getText().toString());
                regimen.setExerciseDescription("Exercise : "+regimenExercise.getText().toString());
                regimen.setDate(regimenDate.getText().toString() + " " + regimenTime.getText().toString());
                databaseHandler.add(regimen, userName);
                Toast.makeText(RegimenActivity.this, " Diet "+regimen.getDietDescription()+" exercise "+ regimen.getExerciseDescription() + " Date "+ regimen.getDate() + " Added!",
                        Toast.LENGTH_LONG).show();
            }
        });

        userName =  getIntent().getStringExtra("userName");
        Intent intentToPassVarToAlarmReceiver = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        intentToPassVarToAlarmReceiver.putExtra("userName", userName);
        sendBroadcast(intentToPassVarToAlarmReceiver);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("user", userName);
        edit.commit();

        if(getIntent().getIntExtra("regimenId",-1) == -1){
            updateRegimen.setEnabled(false);
        }else{
            addRegimen.setEnabled(false);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendNotification() throws InterruptedException {
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Calendar cal = Calendar.getInstance();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                cal.add(Calendar.SECOND, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }}, 0,1000*60*60*24);

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
