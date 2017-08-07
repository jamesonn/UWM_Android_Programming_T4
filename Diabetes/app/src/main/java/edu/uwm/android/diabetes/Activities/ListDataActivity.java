package edu.uwm.android.diabetes.Activities;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import java.util.ArrayList;
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
    CheckBox exerciseCheckBox, dietCheckBox, medicineCheckBox, bglCheckBox;
    RecyclerView recyclerView;
    DataAdapter adapter;
    EditText dateFrom, dateTo;
    Calendar calendar;
    int fromDay, fromMonth, fromYear, toDay, toMonth,toYear;
    Button searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        db = new DatabaseHandler(this);
        userName = getIntent().getStringExtra("userName");

        exerciseCheckBox = (CheckBox) findViewById(R.id.exerciseCheckBox);
        exerciseCheckBox.setChecked(true);
        exerciseCheckBox.setOnCheckedChangeListener(this);
        dietCheckBox = (CheckBox) findViewById(R.id.dietCheckBox);
        dietCheckBox.setChecked(true);
        dietCheckBox.setOnCheckedChangeListener(this);
        medicineCheckBox = (CheckBox) findViewById(R.id.medicineCheckBox);
        medicineCheckBox.setChecked(true);
        medicineCheckBox.setOnCheckedChangeListener(this);
        bglCheckBox = (CheckBox) findViewById(R.id.bglCheckBox);
        bglCheckBox.setChecked(true);
        bglCheckBox.setOnCheckedChangeListener(this);

        calendar = Calendar.getInstance();
        fromDay = calendar.get(Calendar.DAY_OF_MONTH);
        fromMonth = calendar.get(Calendar.MONTH);
        fromYear = calendar.get(Calendar.YEAR);

        toDay = calendar.get(Calendar.DAY_OF_MONTH);
        toMonth = calendar.get(Calendar.MONTH);
        toYear = calendar.get(Calendar.YEAR);

        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateFrom.setText(fromMonth+1+"/"+fromDay+"/"+fromYear);
        dateTo = (EditText) findViewById(R.id.dateTo);
        dateTo.setText(toMonth+1+"/"+toDay+"/"+toYear);



        searchBtn = (Button) findViewById(R.id.buttonSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.filterList(filterByType(filterByDate(objects)));
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

        if (regimenCursor.moveToFirst()) {
            do {
                if (regimenCursor.getString(1).equals(userName)) {
                    Regimen regimenObject = new Regimen();
                    regimenObject.setID(Integer.parseInt(regimenCursor.getString(0)));
                    regimenObject.setDescription(regimenCursor.getString(2));
                    regimenObject.setDate(regimenCursor.getString(3));
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
                        intent.putExtra("regimenDescription", regimen.getDescription());
                        intent.putExtra("regimenDate", regimen.getDate());
                        intent.putExtra("userName", userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                }

            }
        });


    }

    //Two date dialogs, one for From EditText and one for To EditText
    private void DateDialogFrom() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateFrom.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, fromYear, fromMonth, fromDay);
        dpDialog.show();
    }
    private void DateDialogTO() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTo.setText(monthOfYear+1 + "/" + dayOfMonth + "/" + year);
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

    //returns a filtered list by the date (From and To EditTexts)
    private ArrayList<IDatabaseObject> filterByDate(ArrayList<IDatabaseObject> objects) {
        ArrayList<IDatabaseObject> filterdData = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
                if(isAfterDate(dateFrom.getText().toString(),objects.get(i).getDate())&&
                        isBeforeDate(dateTo.getText().toString(),objects.get(i).getDate()))
                    filterdData.add(objects.get(i));

        }
        return filterdData;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


        //comparing between dates
        public static boolean isAfterDate(String fromDate, String dataBaseDate) {
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
    public static boolean isBeforeDate(String toDate, String dataBaseDate) {
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
}