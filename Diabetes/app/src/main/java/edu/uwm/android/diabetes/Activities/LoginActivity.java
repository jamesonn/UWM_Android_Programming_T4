package edu.uwm.android.diabetes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.Database.User;
import edu.uwm.android.diabetes.R;

/**
 * Created by Rafa on 7/24/2017.
 */

public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    Button login;
    EditText userName, password;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signUp = (TextView) findViewById(R.id.signUp);
        login = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        databaseHandler = new DatabaseHandler(this);
        System.out.println("This is called man!");

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
                    Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_LONG).show();
                }else{
                    User user = new User();
                    user = databaseHandler.getUserByUserName(userName.getText().toString());
                    System.out.println("-----> Entered "+password+ " userDB "+user.getPassword());
                    if(user.getPassword().equals(password.getText().toString())){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
