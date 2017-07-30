package edu.uwm.android.diabetes.Activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import edu.uwm.android.diabetes.DataAdapter;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Medicine;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;
import edu.uwm.android.diabetes.R;

public class ListDataActivity extends AppCompatActivity {

    DatabaseHandler db;
    private ArrayList<IDatabaseObject> objects = new ArrayList<IDatabaseObject>();
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        db= new DatabaseHandler(this);
        userName = getIntent().getStringExtra("userName");

        Exercise exercise = new Exercise();
        Cursor cursor1 = db.getData(exercise);
        Medicine medicine = new Medicine();
        Cursor cursor2 = db.getData(medicine);


        if (cursor1.moveToFirst()) {
            do {
                if (cursor1.getString(1).equals(userName)) {
                    Exercise e = new Exercise();
                    e.setID(Integer.parseInt(cursor1.getString(0)));
                    e.setDescription(cursor1.getString(2));
                    e.setDate(cursor1.getString(3));
                    objects.add(e);
                }
            }while (cursor1.moveToNext());
        }else {Log.w("List Data Activity","Cursor1 Empty");}

        if (cursor2.moveToFirst()) {
            do {
                if (cursor2.getString(1).equals(userName)) {
                    Medicine m = new Medicine();
                    m.setID(Integer.parseInt(cursor2.getString(0)));
                    m.setDescription(cursor2.getString(2));
                    m.setDate(cursor2.getString(3));
                    objects.add(m);
                }
            }while(cursor2.moveToNext());
        }else {Log.w("List Data Activity","Cursor2 Empty");}


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListData);
        DataAdapter adapter = new DataAdapter(this, objects);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
