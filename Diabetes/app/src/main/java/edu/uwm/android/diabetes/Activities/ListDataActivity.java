package edu.uwm.android.diabetes.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

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

public class ListDataActivity extends AppCompatActivity {

    DatabaseHandler db;
    private ArrayList<IDatabaseObject> objects = new ArrayList<IDatabaseObject>();
    String userName;
    public DataAdapter adapter;

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
        Diet diet= new Diet();
        Cursor cursor3 = db.getData(diet);
        BloodGlucose bgl = new BloodGlucose();
        Cursor cursor4 = db.getData(bgl);

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

        if (cursor3.moveToFirst()) {
            do {
                if (cursor3.getString(1).equals(userName)) {
                    Diet d = new Diet();
                    d.setID(Integer.parseInt(cursor3.getString(0)));
                    d.setDescription(cursor3.getString(2));
                    d.setDate(cursor3.getString(3));
                    objects.add(d);
                }
            }while(cursor3.moveToNext());
        }else {Log.w("List Data Activity","Cursor3 Empty");}

        if (cursor4.moveToFirst()) {
            do {
                if (cursor4.getString(1).equals(userName)) {
                    BloodGlucose b = new BloodGlucose();
                    b.setID(Integer.parseInt(cursor4.getString(0)));
                    b.setValue(Double.parseDouble(cursor4.getString(2)));
                    b.setDate(cursor4.getString(3));
                    objects.add(b);
                }
            }while(cursor4.moveToNext());
        }else {Log.w("List Data Activity","Cursor4 Empty");}


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewListData);
        final DataAdapter adapter = new DataAdapter(this, objects);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        adapter.setOnItemLongClickListener(new DataAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position) {
                Intent intent;
                String type = objects.get(position).getClassID();
                switch (type){
                   case Constants.EXERCISE_CLASS:
                       Exercise exercise = (Exercise) objects.get(position);
                       intent = new Intent(ListDataActivity.this, ExerciseActivity.class );
                       intent.putExtra("exerciseId", exercise.getID());
                       intent.putExtra("exerciseDescription",exercise.getDescription());
                       intent.putExtra("exerciseDate",exercise.getDate());
                       intent.putExtra("userName",userName);
                       startActivity(intent);
                       ListDataActivity.this.finish();
                       break;
                    case Constants.DIET_CLASS:
                        Diet diet = (Diet) objects.get(position);
                        intent = new Intent(ListDataActivity.this, DActivity.class );
                        intent.putExtra("dietId", diet.getID());
                        intent.putExtra("dietDescription",diet.getDescription());
                        intent.putExtra("dietDate",diet.getDate());
                        intent.putExtra("userName",userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.BLOODGLUCOSE_CLASS:
                        BloodGlucose bloodGlucose = (BloodGlucose) objects.get(position);
                        intent = new Intent(ListDataActivity.this, BGLActivity.class );
                        intent.putExtra("bglId", bloodGlucose.getID());
                        intent.putExtra("bglValue",bloodGlucose.getValue());
                        intent.putExtra("bglDate",bloodGlucose.getDate());
                        intent.putExtra("userName",userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                    case Constants.MEDICINE_CLASS:
                        Medicine medicine = (Medicine) objects.get(position);
                        intent = new Intent(ListDataActivity.this, MedicineActivity.class );
                        intent.putExtra("medicineId", medicine.getID());
                        intent.putExtra("medicineDescription",medicine.getDescription());
                        intent.putExtra("medicineDate",medicine.getDate());
                        intent.putExtra("userName",userName);
                        startActivity(intent);
                        ListDataActivity.this.finish();
                        break;
                }

            }
        });
    }
}
