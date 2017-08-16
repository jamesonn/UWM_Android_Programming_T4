package edu.uwm.android.diabetes.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHandler databaseHandler;
    TextView welcomeMessage;
    TextView welcomeMenuMessage;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calling the database
        SQLCipherInit();
        databaseHandler = new DatabaseHandler(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        userName =  getIntent().getStringExtra("userName");
        welcomeMessage.setText("Welcome "+userName);

        RelativeLayout BGL = (RelativeLayout)findViewById(R.id.BGLL);
        RelativeLayout exercise = (RelativeLayout)findViewById(R.id.EXERCISEL);
        RelativeLayout diet = (RelativeLayout)findViewById(R.id.DIETL);
        RelativeLayout med = (RelativeLayout)findViewById(R.id.MEDL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BGL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, BGLActivity.class );
                intent.putExtra("userName", userName );
                startActivity(intent);
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ExerciseActivity.class );
                intent.putExtra("userName", userName );
                startActivity(intent);
            }
        });

        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, DietActivity.class );
                intent.putExtra("userName", userName );
                startActivity(intent);
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, MedicineActivity.class );
                intent.putExtra("userName", userName );
                startActivity(intent);
            }
        });
    }

    private void SQLCipherInit(){
        SQLiteDatabase.loadLibs(this);
        File dbFile = getDatabasePath("diabetes1.db");
        dbFile.mkdirs();
        dbFile.delete();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, "password", null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        welcomeMenuMessage = (TextView) findViewById(R.id.welcomeMenuMessage);
        welcomeMenuMessage.setText("Welcome "+userName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();

         if (id == R.id.nav_graph) {
            intent = new Intent(this, FragmentActivity.class );
            intent.putExtra("userName", userName );
        }  else if (id == R.id.nav_regimen) {
             intent = new Intent(this, RegimenActivity.class );
             intent.putExtra("userName", userName );
        } else if (id == R.id.nav_List){
             intent = new Intent(this, ListDataActivity.class);
             intent.putExtra("userName", userName );
        } else if (id == R.id.nav_about) {
             intent = new Intent(this, AboutActivity.class);
             intent.putExtra("userName", userName );
         } else if (id == R.id.logout){
             intent = new Intent(this, LoginActivity.class);
             SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
             SharedPreferences.Editor editor = sp.edit();
             editor.putBoolean("loggedin",false);
             editor.putString("name", "");
             editor.commit();
             intent.putExtra("userName", "" );
             finish();
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHandler.close();
    }
}
