package edu.uwm.android.diabetes;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.Diet;
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
                viewHolder.typeTextView.setText(R.string.activity_Exercise);
                viewHolder.infoTextView.setText(exercise.getDescription());
                viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ExerciseThemeColoe));
                viewHolder.dateTextView.setText(exercise.getDate());
                break;
            case Constants.MEDICINE_CLASS:
                Medicine medicine= (Medicine) object;
                viewHolder.typeTextView.setText(R.string.activity_Medicine);
                viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.MedicationThemeColor));
                viewHolder.infoTextView.setText(medicine.getDescription());
                viewHolder.dateTextView.setText(medicine.getDate());
                break;
            case Constants.DIET_CLASS:
                Diet diet= (Diet) object;
                viewHolder.typeTextView.setText("Diet");
                viewHolder.infoTextView.setText(diet.getDescription());
                viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                viewHolder.dateTextView.setText(diet.getDate());
                break;
            case Constants.BLOODGLUCOSE_CLASS:
                BloodGlucose bgl= (BloodGlucose) object;
                viewHolder.layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.BGLThemeColor));
                viewHolder.typeTextView.setText("BGL");
                viewHolder.infoTextView.setText(String.valueOf(bgl.getValue()));
                viewHolder.dateTextView.setText(bgl.getDate());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    //-------------- View Holder below ---------------------------------

    //View holder class for all the objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public TextView typeTextView;
        public TextView infoTextView; //value for BGL, and description for the others
        public TextView dateTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.linearLayoutHorizontal);
            typeTextView = (TextView) itemView.findViewById(R.id.TextView_type);
            infoTextView = (TextView) itemView.findViewById(R.id.TextView_info);
            dateTextView = (TextView) itemView.findViewById(R.id.TextView_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}

