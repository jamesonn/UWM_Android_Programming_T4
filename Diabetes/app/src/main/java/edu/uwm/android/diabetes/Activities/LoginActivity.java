package edu.uwm.android.diabetes.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.User;
import edu.uwm.android.diabetes.R;

/**
 * Created by Rafa on 7/25/2017.
 */

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    Button login;
    EditText userName, password;
    DatabaseHandler databaseHandler;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUp = (TextView) findViewById(R.id.signUp);
        login = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        databaseHandler = new DatabaseHandler(this);
        rememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);
        showSharedPreferences();

        //we need to call this on load for SQLCipher
        SQLiteDatabase.loadLibs(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //See if user exists
                if(!databaseHandler.isUserRegistered(userName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_SHORT).show();
                }else{
                    User user;
                    user = databaseHandler.getUserByUserName(userName.getText().toString());
                    if(user.getPassword().equals(password.getText().toString())){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("userName", user.getUserName());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //use this inside onPause()
    public void saveSharedPreferences(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", userName.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.putBoolean("loggedin",true);
        if(rememberMe.isChecked())
            editor.putString("checkBox","checked");
        else editor.putString("checkBox","");
        editor.commit();
    }

    //use this inside onCreate()
    public void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        if(!sp.equals(null)) {
//            userName.setText(sp.getString("name", ""));
//            password.setText(sp.getString("password", ""));
//            if(sp.getString("checkBox","").equals("checked"))
//            rememberMe.setChecked(true);
//        }

        Boolean islog = sp.getBoolean("loggedin", false);
        if (islog) {
            if (sp.getString("checkBox", "").equals("checked")) {
                //rememberMe.setChecked(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userName", sp.getString("name", ""));
                startActivity(intent);
                finish();
            }
        }
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
        if(rememberMe.isChecked()){
            saveSharedPreferences();
        }else{
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }
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
