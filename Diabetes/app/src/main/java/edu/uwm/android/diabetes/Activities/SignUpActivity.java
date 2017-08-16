package edu.uwm.android.diabetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.User;
import edu.uwm.android.diabetes.R;

/**
 * Created by Rafa on 7/25/2017.
 */

public class SignUpActivity extends AppCompatActivity {
    EditText userName, password, confirmPassword;
    Button signUp;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userName = (EditText) findViewById(R.id.signUpUsername);
        password = (EditText) findViewById(R.id.signUpPassword1);
        confirmPassword = (EditText) findViewById(R.id.signUpPassword2);
        signUp = (Button) findViewById(R.id.signUpButton);
        databaseHandler = new DatabaseHandler(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) { //UserName used condition should be added
                    User user = new User();
                    user.setUserName(userName.getText().toString());
                    user.setPassword(password.getText().toString());
                    databaseHandler.addUser(user);
                    Toast.makeText(getApplicationContext(), "User "+user.getUserName()+ " added successfully.", Toast.LENGTH_LONG).show();
                    Intent backToLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(backToLogin);
                }else{
                    Toast.makeText(getApplicationContext(), "The two passwords are not compatible", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
