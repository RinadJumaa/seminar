package edu.cs.sm.UserLogIn_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs.sm.R;
import edu.cs.sm.UserTasks.SecondActivity;

public class UserRegisterSqlite extends AppCompatActivity {

    EditText username, email, password, confirmpassword;
    TextView signinPage;
    Button register;
    DBhelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sqlite);
        getSupportActionBar().hide();
        setUpViews();

        //switch to the register page
        signinPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegisterSqlite.this,
                        UserLoginSqlite.class);
                startActivity(intent);
            }
        });
        DB.getData(); // this line prints all users in database on the console

    }

    private void setUpViews() {

        username = findViewById(R.id.etFullName);
        email = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        confirmpassword = findViewById(R.id.etConfirmPassword);
        signinPage = findViewById(R.id.btnRegisterLogin);
        register = findViewById(R.id.btnRegister);
        DB = new DBhelper(this);
    }

    public void registerUser(View view) {
        String name = username.getText().toString();
        String emailstr = email.getText().toString();
        String pass = password.getText().toString();
        String repass = confirmpassword.getText().toString();

        //if any of the edittext is empty a toast will pop up
        if (name.equals("") || emailstr.equals("") || 
        pass.equals("") || repass.equals("")){

            Toast.makeText(this, "fields should not be empty", Toast.LENGTH_SHORT).show();
        }

        else {
            if (pass.equals(repass)){ // the password field and confirm password field are the same
                Boolean checkuser = DB.checkemail(emailstr); // checks if there is a user registered using this email before
                if (checkuser == false){ // there is no user registered with that email
                    Boolean insert = DB.insertData(name, emailstr, pass); // insert data into the table
                    if (insert == true) { // if it user registered successfully switch to home activity
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserRegisterSqlite.this, SecondActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "User already exist", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "passwords not matching", Toast.LENGTH_SHORT).show();
        }
        
    }
}