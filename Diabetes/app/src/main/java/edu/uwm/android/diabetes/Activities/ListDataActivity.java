package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import edu.uwm.android.diabetes.Constants;
import edu.uwm.android.diabetes.DataAdapter;
import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Diet;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Medicine;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;
import edu.uwm.android.diabetes.R;

public class ListDataActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    DatabaseHandler db;
    private ArrayList<IDatabaseObject> objects = new ArrayList<IDatabaseObject>();
    String userName;
    CheckBox exerciseCheckBox, dietCheckBox, medicineCheckBox, bglCheckBox, regimenCheckBox;
    RecyclerView recyclerView;
    DataAdapter adapter;
    EditText dateFrom, dateTo, timeFrom, timeTo, bglValueFrom, bglValueTo, contaiensKeyWords;
    Calendar calendar;
    int fromDay, fromMonth, fromYear, fromHour, fromMinutes, toDay, toMonth, toYear, toHour, toMinutes;
    Button searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        db = new DatabaseHandler(this);
        userName = getIntent().getStringExtra("userName");

        contaiensKeyWords = (EditText) findViewById(R.id.contaiensKeyWords);
        regimenCheckBox =(CheckBox) findViewById(R.id.regimenCheckBox);
        regimenCheckBox.setChecked(true);
        exerciseCheckBox = (CheckBox) findViewById(R.id.exerciseCheckBox);
        exerciseCheckBox.setChecked(true);
        dietCheckBox = (CheckBox) findViewById(R.id.dietCheckBox);
        dietCheckBox.setChecked(true);
        medicineCheckBox = (CheckBox) findViewById(R.id.medicineCheckBox);
        medicineCheckBox.setChecked(true);
        bglCheckBox = (CheckBox) findViewById(R.id.bglCheckBox);
        bglCheckBox.setChecked(true);
        bglCheckBox.setOnCheckedChangeListener(this);

        bglValueFrom = (EditText) findViewById(R.id.bglValuFrom);
        bglValueFrom.setText("40.0");
        bglValueTo = (EditText) findViewById(R.id.bglValuTo);
        bglValueTo.setText("600.0");


        calendar = Calendar.getInstance();
        fromDay = calendar.get(Calendar.DAY_OF_MONTH);
        fromMonth = calendar.get(Calendar.MONTH);
        fromYear = calendar.get(Calendar.YEAR);
        fromHour = calendar.get(Calendar.HOUR_OF_DAY);
        fromMinutes = calendar.get(Calendar.MINUTE);

        toDay = calendar.get(Calendar.DAY_OF_MONTH);
        toMonth = calendar.get(Calendar.MONTH);
        toYear = calendar.get(Calendar.YEAR);
        toHour = calendar.get(Calendar.HOUR_OF_DAY);
        toMinutes = calendar.get(Calendar.MINUTE);

        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateFrom.setText(fromMonth -3 + "/" + fromDay + "/" + fromYear);
        dateTo = (EditText) findViewById(R.id.dateTo);
        dateTo.setText(toMonth + 1 + "/" + toDay + "/" + toYear);
        timeFrom = (EditText) findViewById(R.id.timeFrom);
        timeFrom.setText("00:00");
        timeTo = (EditText) findViewById(R.id.timeTo);
        timeTo.setText("23:59");

        searchBtn = (Button) findViewById(R.id.buttonSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bglValueFrom.isEnabled() && bglValueTo.isEnabled()) {
                    adapter.filterList(filterByType(filterByDate(filterByTime(filterByBglValue(filterByKeyWords(objects))))));
                } else {
                    adapter.filterList(filterByType(filterByDate(filterByTime(filterByKeyWords(objects)))));
                }
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogTO();
            }
        });
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFrom();
            }
        });
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ListDataActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeFrom.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ListDataActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeTo.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        Exercise exercise = new Exercise();
        Cursor cursor1 = db.getData(exercise);
        Medicine medicine = new Medicine();
        Cursor cursor2 = db.getData(medicine);
        Diet diet = new Diet();
        Cursor cursor3 = db.getData(diet);
        BloodGlucose bgl = new BloodGlucose();
        Cursor cursor4 = db.getData(bgl);
        Regimen regimen = new Regimen();
        Cursor regimenCursor = db.getData(regimen);

        if (cursor4.moveToFirst()) {
            do {
                if (cursor4.getString(1).equals(userName)) {
                    BloodGlucose b = new BloodGlucose();
                    b.setID(Integer.parseInt(cursor4.getString(0)));
                    b.setValue(Double.parseDouble(cursor4.getString(2)));
                    b.setDate(cursor4.getString(3));
                    objects.add(b);
                }
            } while (cursor4.moveToNext());
        } else {
            Log.w("List Data Activity", "Cursor4 Empty");
        }

        if (cursor3.moveToFirst()) {
            do {
                if (cursor3.getString(1).equals(userName)) {
                    Diet d = new Diet();
                    d.setID(Integer.parseInt(cursor3.getString(0)));
                    d.setDescription(cursor3.getString(2));
                    d.setDate(cursor3.getString(3));
                    objects.add(d);
                }
            } while (cursor3.moveToNext());
        } else {
            Log.w("List Data Activity", "Cursor3 Empty");
        }

        if (cursor1.moveToFirst()) {
            do {
                if (cursor1.getString(1).equals(userName)) {
                    Exercise e = new Exercise();
                    e.setID(Integer.parseInt(cursor1.getString(0)));
                    e.setDescription(cursor1.getString(2));
                    e.setDate(cursor1.getString(3));
                    objects.add(e);
                }
            } while (cursor1.moveToNext());
        } else {
            Log.w("List Data Activity", "Cursor1 Empty");
        }

        if (cursor2.moveToFirst()) {
            do {
                if (cursor2.getString(1).equals(userName)) {
                    Medicine m = new Medicine();
                    m.setID(Integer.parseInt(cursor2.getString(0)));
                    m.setDescription(cursor2.getString(2));
                    m.setDate(cursor2.getString(3));
                    objects.add(m);
                }
            } while (cursor2.moveToNext());
        } else {
            Log.w("List Data Activity", "Cursor2 Empty");
        }


        if (regimenCursor.moveToFirst()) {
            do {
                if (regimenCursor.getString(1).equals(userName)) {
                    Regimen regimenObject = new Regimen();
                    regimenObject.setID(Integer.parseInt(regimenCursor.getString(0)));
                    regimenObject.setExerciseDescription(regimenCursor.getString(2));
                    regimenObject.setDietDescription(regimenCursor.getString(3));
                    regimenObject.setDate(regimenCursor.getString(4));

                    objects.add(regimenObject);
                }
            } while (regimenCursor.moveToNext());
        } else {
            Log.w("List Data Activity", "regimenCursor Empty");
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListData);
        adapter = new DataAdapter(this, objects);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //delete by fast click
        adapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String type = objects.get(position).getClassID();
                db.delete(objects.get(position));
                objects.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                Toast.makeText(ListDataActivity.this, type + " was deleted", Toast.LENGTH_SHORT).show();
            }

        });

        //update by long click
        adapter.setOnItemLongClickListener(new DataAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position) {
                Intent intent;
                String type = objects.get(position).getClassID();
                switch (type) {
                    case Constants.EXERCISE_CLASS:
                        Exercise exercise = (Exercise) objects.get(position);
                        intent = new Intent(ListDataActivity.this, ExerciseActivity.class);
                        intent.putExtra("exerciseId", exercise.getID());
                        intent.putExtra("exerciseDescription", exercise.getDescription());
                        intent.putExtra("exerciseDate", exercise.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.DIET_CLASS:
                        Diet diet = (Diet) objects.get(position);
                        intent = new Intent(ListDataActivity.this, DietActivity.class);
                        intent.putExtra("dietId", diet.getID());
                        intent.putExtra("dietDescription", diet.getDescription());
                        intent.putExtra("dietDate", diet.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.BLOODGLUCOSE_CLASS:
                        BloodGlucose bloodGlucose = (BloodGlucose) objects.get(position);
                        intent = new Intent(ListDataActivity.this, BGLActivity.class);
                        intent.putExtra("bglId", bloodGlucose.getID());
                        intent.putExtra("bglValue", bloodGlucose.getValue());
                        intent.putExtra("bglDate", bloodGlucose.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.MEDICINE_CLASS:
                        Medicine medicine = (Medicine) objects.get(position);
                        intent = new Intent(ListDataActivity.this, MedicineActivity.class);
                        intent.putExtra("medicineId", medicine.getID());
                        intent.putExtra("medicineDescription", medicine.getDescription());
                        intent.putExtra("medicineDate", medicine.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.REGIMEN_CLASS:
                        Regimen regimen = (Regimen) objects.get(position);
                        intent = new Intent(ListDataActivity.this, RegimenActivity.class);
                        intent.putExtra("regimenId", regimen.getID());
                        intent.putExtra("regimenDescription", regimen.getExerciseDescription());

                        intent.putExtra("regimenDate", regimen.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                }

            }
        });

        cursor1.close();
    }

    //Two date dialogs, one for From EditText and one for To EditText
    private void DateDialogFrom() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateFrom.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, fromYear, fromMonth, fromDay);
        dpDialog.show();
    }

    private void DateDialogTO() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTo.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, fromYear, fromMonth, fromDay);
        dpDialog.show();
    }


    //returns a filtered list by the type(checkboxes)
    private ArrayList<IDatabaseObject> filterByType(ArrayList<IDatabaseObject> objects) {
        //new array list that will hold the filtered data
        ArrayList<IDatabaseObject> filterdDataByType = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if(regimenCheckBox.isChecked()){
                if (objects.get(i).getClassID().equals(Constants.REGIMEN_CLASS)){
                    filterdDataByType.add(objects.get(i));
                }
            }
            if (exerciseCheckBox.isChecked()) {
                if (objects.get(i).getClassID().equals(Constants.EXERCISE_CLASS)) {
                    filterdDataByType.add(objects.get(i));
                }
            }
            if (dietCheckBox.isChecked()) {
                if (objects.get(i).getClassID().equals(Constants.DIET_CLASS)) {
                    filterdDataByType.add(objects.get(i));
                }
            }
            if (medicineCheckBox.isChecked()) {
                if (objects.get(i).getClassID().equals(Constants.MEDICINE_CLASS)) {
                    filterdDataByType.add(objects.get(i));
                }
            }
            if (bglCheckBox.isChecked()) {
                if (objects.get(i).getClassID().equals(Constants.BLOODGLUCOSE_CLASS)) {
                    filterdDataByType.add(objects.get(i));
                }
            }
        }
        return filterdDataByType;
    }

    //returns a filtered list by the date (FromDate and ToDate EditTexts)
    private ArrayList<IDatabaseObject> filterByDate(ArrayList<IDatabaseObject> objects) {
        ArrayList<IDatabaseObject> filterdData = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterDate(dateFrom.getText().toString(), objects.get(i).getDate()) &&
                    isBeforeDate(dateTo.getText().toString(), objects.get(i).getDate()))
                filterdData.add(objects.get(i));

        }
        return filterdData;
    }

    //returns a filtered list by the Time (FromTime and ToTime EditTexts)
    private ArrayList<IDatabaseObject> filterByTime(ArrayList<IDatabaseObject> objects) {
        ArrayList<IDatabaseObject> filterdTime = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterTime(timeFrom.getText().toString(), objects.get(i).getDate()) &&
                    !isAfterTime(timeTo.getText().toString(), objects.get(i).getDate()))
                filterdTime.add(objects.get(i));
        }
        return filterdTime;
    }

    //returns a filtered list by the Bgl Values(From and To EditTexts)
    private ArrayList<IDatabaseObject> filterByBglValue(ArrayList<IDatabaseObject> objects) {
        ArrayList<IDatabaseObject> filterdBglValue = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getClassID().equals(Constants.BLOODGLUCOSE_CLASS)) {
                BloodGlucose bgl = (BloodGlucose) objects.get(i);
                if (isBetweenBglValue(bglValueFrom.getText().toString(), bgl.getValue(), bglValueTo.getText().toString())) {
                    filterdBglValue.add(objects.get(i));
                }
            } else {
                filterdBglValue.add(objects.get(i));
            }
        }
        return filterdBglValue;
    }

    private ArrayList<IDatabaseObject> filterByKeyWords(ArrayList<IDatabaseObject> objects) {
        ArrayList<IDatabaseObject> filterdKeyWords = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (!objects.get(i).getClassID().equals(Constants.BLOODGLUCOSE_CLASS)) {
                String type = objects.get(i).getClassID();
                switch (type) {
                    case Constants.REGIMEN_CLASS:
                        Regimen regimen = (Regimen) objects.get(i);
                        if(containsWords(contaiensKeyWords.getText().toString(),regimen.getDietDescription()) ||
                                containsWords( contaiensKeyWords.getText().toString().toString(),regimen.getExerciseDescription())){
                            filterdKeyWords.add(objects.get(i));
                        }
                        break;
                    case Constants.EXERCISE_CLASS:
                        Exercise exercise = (Exercise) objects.get(i);
                        if (containsWords(contaiensKeyWords.getText().toString(), exercise.getDescription())) {
                            filterdKeyWords.add(objects.get(i));
                        }
                        break;
                    case Constants.DIET_CLASS:
                        Diet diet = (Diet) objects.get(i);
                        if (containsWords(contaiensKeyWords.getText().toString(), diet.getDescription())) {
                            filterdKeyWords.add(objects.get(i));
                        }
                        break;
                    case Constants.MEDICINE_CLASS:
                        Medicine medicine = (Medicine) objects.get(i);
                        if (containsWords(contaiensKeyWords.getText().toString(), medicine.getDescription())) {
                            filterdKeyWords.add(objects.get(i));
                        }
                        break;
                }
            } else filterdKeyWords.add(objects.get(i));
        }
            return filterdKeyWords;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (bglCheckBox.isChecked()) {
            bglValueFrom.setEnabled(true);
            bglValueTo.setEnabled(true);
            bglValueFrom.setText("40.0");
            bglValueTo.setText("600.0");
        } else {
            bglValueTo.setEnabled(false);
            bglValueFrom.setEnabled(false);
            bglValueFrom.getText().clear();
            bglValueTo.getText().clear();

        }

    }


    //comparing between dates
    private boolean isAfterDate(String fromDate, String dataBaseDate) {
        String[] dateOne = fromDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        String[] year1 = dateTwo[2].split(" ");
        if (Integer.parseInt(dateOne[2]) > Integer.parseInt(year1[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) < Integer.parseInt(year1[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[1]) > Integer.parseInt(dateTwo[1])) {
            return false;
        } else return true;

    }

    private boolean isBeforeDate(String toDate, String dataBaseDate) {
        String[] dateOne = toDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        String[] year1 = dateTwo[2].split(" ");
        if (Integer.parseInt(dateOne[2]) < Integer.parseInt(year1[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) > Integer.parseInt(year1[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return true;
        } else if (Integer.parseInt(dateOne[1]) < Integer.parseInt(dateTwo[1])) {
            return false;
        } else return true;
    }



    //comparing between times
    private boolean isAfterTime(String fromTime, String databaseTime) {
        String[] timeOne = fromTime.split(":");
        String[] date = databaseTime.split("/");
        String[] YearAndTime = date[2].split(" ");
        String[] time = YearAndTime[1].split(":");
        Log.w("Values of YearAndTime", YearAndTime[0] + " " + YearAndTime[1]);
        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        if (Integer.parseInt(timeOne[0]) < hour) {
            return true;
        } else if (Integer.parseInt(timeOne[0]) > hour) {
            return false;
        } else return (Integer.parseInt(timeOne[1]) <= minutes);
    }

    //comparing between bgl values
    private boolean isBetweenBglValue(String bglValueFrom, Double bglValueData, String bglValueTo) {
        Double valueFrom = Double.parseDouble(bglValueFrom);
        Double valueTo = Double.parseDouble(bglValueTo);

        if (bglValueData < valueFrom || bglValueData > valueTo)
            return false;
        return true;
    }

    //comparing using key words
    private boolean containsWords(String fromEditText, String fromData) {
        ArrayList<String> stringOne = new ArrayList<String>(Arrays.asList(fromEditText.split(" ")));
        ArrayList<String> stringTwo = new ArrayList<String>(Arrays.asList(fromData.split(" ")));
        if(fromEditText.equals("") || fromEditText.equals(null))
            return true;

        if (stringOne.contains("AND")) {
            if (fromData.contains(stringOne.get(0)) && fromData.contains(stringOne.get(2))) {
                return true;
            } else return false;
        } else if (stringOne.contains("OR")) {
            if (fromData.contains(stringOne.get(0)) || fromData.contains(stringOne.get(2))) {
                return true;
            } else return false;
        } else if (fromData.contains(fromEditText)) {
            return true;
        } else return false;
    }
}