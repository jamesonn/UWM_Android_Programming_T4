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
import edu.uwm.android.diabetes.R;

public class ListDataActivity extends AppCompatActivity {

    DatabaseHandler db;
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        db= new DatabaseHandler(this);
        Exercise exercise = new Exercise();
        Cursor cursor = db.getData(exercise);

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Exercise e = new Exercise();
                e.setID(Integer.parseInt(cursor.getString(0)));
                e.setDescription(cursor.getString(1));
                e.setDate(cursor.getString(2));
                exercises.add(e);
            }
        }else {Log.w("List Data Activity","Empty");}

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListData);
        DataAdapter adapter = new DataAdapter(this, exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
