package edu.uwm.android.diabetes.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseHandler databaseHandler;
    //ArrayList<double> dataList;
    double[] valBGL = new double[1000000];

    //
    TextView Ave;
    TextView Min;
    TextView Max;
    TextView Med;


    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();


        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        Ave = (TextView) rootView.findViewById(R.id.textViewAve);
        Min = (TextView) rootView.findViewById(R.id.textViewMin);
        Max = (TextView) rootView.findViewById(R.id.textViewMax);
        Med = (TextView) rootView.findViewById(R.id.textViewMedian);



        Ave.setText(String.valueOf(""+averageBGL()));
        Min.setText(String.valueOf(""+minBGL()));
        Max.setText(String.valueOf(""+maxBGL()));
        Med.setText(String.valueOf(""+medBGL()));
        // Inflate the layout for this fragment
        return rootView;
    }

    public double averageBGL() {
        double sum = 0.0;
        int counter = 0;
        Cursor cursor = databaseHandler.getData(new BloodGlucose());
        if(cursor.moveToFirst()){
            do{
                sum+= Double.parseDouble(cursor.getString(2));
                counter++;
            }while (cursor.moveToNext());
        }else{return -1.0;}
        cursor.close();
        databaseHandler.close();
        return sum / counter;
    }

    public double minBGL() {
        double temp;
        int counter = 0;
        Cursor cursor = databaseHandler.getData(new BloodGlucose());

        if(cursor.moveToFirst()){
            temp = Double.parseDouble(cursor.getString(2));
            cursor.moveToNext();
            do{
                if (temp > Double.parseDouble(cursor.getString(2))){
                    temp = Double.parseDouble(cursor.getString(2));
                }

            }while (cursor.moveToNext());
        }else{return -1.0;}
        cursor.close();
        databaseHandler.close();
        return temp;
    }

    public double maxBGL() {
        double temp;
        int counter = 0;
        Cursor cursor = databaseHandler.getData(new BloodGlucose());

        if(cursor.moveToFirst()){
            temp = Double.parseDouble(cursor.getString(2));
            cursor.moveToNext();
            do{
                if (temp < Double.parseDouble(cursor.getString(2))){
                    temp = Double.parseDouble(cursor.getString(2));
                }

            }while (cursor.moveToNext());
        }else{return -1.0;}
        cursor.close();
        databaseHandler.close();
        return temp;
    }

    public double medBGL() {
        List<Double> temp = new ArrayList<>();
        int counter = 0;
        Cursor cursor = databaseHandler.getData(new BloodGlucose());

        if(cursor.moveToFirst()) {
            do {
                temp.add(Double.parseDouble(cursor.getString(2)));
                counter++;
            } while (cursor.moveToNext());
        }

        Collections.sort(temp);
        Log.w("temp",temp.toString());

        cursor.close();
        databaseHandler.close();
        if(temp.size()>1) {
            return temp.get((temp.size() / 2));
        }else if(temp.size() == 1){ return temp.get(0);}
        else return 0.0;
    }
}
