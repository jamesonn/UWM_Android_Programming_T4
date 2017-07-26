package edu.uwm.android.diabetes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.uwm.android.diabetes.Activities.ListDataActivity;
import edu.uwm.android.diabetes.Database.Exercise;
import edu.uwm.android.diabetes.Database.Medicine;
import edu.uwm.android.diabetes.Database.Regimen;
import edu.uwm.android.diabetes.Interfaces.IDatabaseObject;

//TODO: A more general adapter. It works for Exercises and Medicines
public class DataAdapter extends
        RecyclerView.Adapter<DataAdapter.ViewHolder> {

    // Store a member variable for the data
    private ArrayList<IDatabaseObject> objects;

    private Context context;

    // Pass in the fruit array into the constructor
    public DataAdapter(Context context, ArrayList<IDatabaseObject> objects) {
        this.objects = objects;
        this.context = context;
    }


    private Context getContext() {
        return context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View dataView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_item_card_layout,parent,false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(dataView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        IDatabaseObject object = objects.get(position);
        String type = object.getClassID();
        switch (type){
            case Constants.EXERCISE_CLASS:
                Exercise exercise = (Exercise) object;
                viewHolder.typeTextView.setText("EXERCISE");
                viewHolder.infoTextView.setText(exercise.getDescription());
                viewHolder.dateTextView.setText(exercise.getDate());
                break;
            case Constants.MEDICINE_CLASS:
                Medicine medicine= (Medicine) object;
                viewHolder.typeTextView.setText("MEDICINE");
                viewHolder.infoTextView.setText(medicine.getDescription());
                viewHolder.dateTextView.setText(medicine.getDate());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }




    //-------------- View Holder below ---------------------------------

    //View holder class for all the objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView typeTextView;
        public TextView infoTextView; //value for BGL, and description for the others
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            typeTextView = (TextView) itemView.findViewById(R.id.TextView_type);
            infoTextView = (TextView) itemView.findViewById(R.id.TextView_info);
            dateTextView = (TextView) itemView.findViewById(R.id.TextView_date);
        }
    }
}

