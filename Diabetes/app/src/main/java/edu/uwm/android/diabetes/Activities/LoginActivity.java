package edu.uwm.android.diabetes.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import edu.uwm.android.diabetes.R;



//TODO I haven't tested, not sure if this works



public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    EditText username, password;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin(username.getText().toString(), password.getText().toString());
            }
        });

        Button newUserButton = (Button) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO prompt user to give username and password
                //createNewUser();
            }
        });
    }

    private void validateAndLogin(String username, String password) {
        if (username != null && password != null) {
            preferences = getSharedPreferences("logins", Context.MODE_PRIVATE);
            String storedPassword = preferences.getString(username, null);
            if (storedPassword != null) {
                if (storedPassword.equals(password)) {
                    //TODO login
                }
            }
        }
    }

    private void createNewUser(String newUsername, String password) {
        preferences = getSharedPreferences("logins", Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = preferences.edit();
        writer.putString(newUsername, password);
        writer.commit();
    }


    //TODO: use this inside onPause()
    private void saveSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();
    }

    //TODO: use this inside onCreate
    private void showSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (!sp.equals(null)) {
            username.setText(sp.getString("name", ""));
            password.setText(sp.getString("number", ""));
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
    protected void onPause() {super.onPause();}

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}