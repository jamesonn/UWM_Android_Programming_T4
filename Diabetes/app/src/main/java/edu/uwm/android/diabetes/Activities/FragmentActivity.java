package edu.uwm.android.diabetes.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import edu.uwm.android.diabetes.Fragments.GraphFragment;
import edu.uwm.android.diabetes.Fragments.StatisticFragment;
import edu.uwm.android.diabetes.R;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);


        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.containerMain);

        if (fragment == null) {
            fragment = new GraphFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.containerMain, fragment);
            transaction.commit();
        }


        Button b1 = (Button)findViewById(R.id.button);
        Button b2 = (Button)findViewById(R.id.button2);
        //Button b3 = (Button)findViewById(R.id.button3);
        FrameLayout fm = (FrameLayout)findViewById(R.id.containerMain);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                GraphFragment graphFragment = new GraphFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.containerMain, graphFragment).commit();

//                    mFragment = new ;
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.add(frame.getId(), mFragment).commit();
//
//
//                setContentView(frame);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                StatisticFragment BFragement = new StatisticFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.containerMain,BFragement).commit();

            }
        });

//        b3.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//
//                GraphFragment mainFragement = new GraphFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.containerMain,mainFragement).addToBackStack(null).commit();
//
//            }
//        });

    }
}
