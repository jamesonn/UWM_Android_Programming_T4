package edu.uwm.android.diabetes.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int maxBgl = 600;
    private static final int minBgl = 40;
    private static final int maxDate = 31;
    private static final int minDate = 1;

    private Cursor cursor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseHandler databaseHandler;


    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        LineChart chart = (LineChart) rootView.findViewById(R.id.chart);
//        chart.setVisibleXRangeMaximum(maxDate);
//        chart.setVisibleXRangeMinimum(minDate);

        Description description = new Description();
        description.setText("BGL values from the last 30 days");
        chart.setDescription(description);



        List<Entry> entries = new ArrayList<Entry>();

        cursor = databaseHandler.getData(new BloodGlucose());
        float data;
        String date;

        Calendar calendar = Calendar.getInstance();

        if (cursor.moveToNext()){
            do{
                data = Float.parseFloat(cursor.getString(2));

                int currentMonth = calendar.get(Calendar.MONTH)+1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                date = cursor.getString(3);

//                if(currentMonth == getMonth(date)){
                    entries.add(new Entry(getDate(date),data));
//                }else{
//                    if((currentMonth-1 == getMonth(date)) && (currentDay>=getDate(date))){
//                        entries.add(new Entry(getDate(date),data));
//                    }
//                }


                //entries.add(new Entry(data,date));
            }while (cursor.moveToNext());
            cursor.close();
        }






//        entries.add(new Entry(1,40));
//        entries.add(new Entry(12,30));
//        entries.add(new Entry(13,50));
//        entries.add(new Entry(14,70));
//        entries.add(new Entry(15,10));
//        entries.add(new Entry(16,60));
//        entries.add(new Entry(31,60));




        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setCircleColor(R.color.MedicationThemeColor);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onPause() {

        super.onPause();
        cursor.close();
        databaseHandler.close();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        cursor.close();
        databaseHandler.close();
    }

    private int getDate (String data){

        String[] ret = data.split("/") ;
        return Integer.parseInt(ret[1]);
    }

    private int getMonth(String data){
        String[] ret = data.split("/") ;
        return Integer.parseInt(ret[0]);

    }
}

