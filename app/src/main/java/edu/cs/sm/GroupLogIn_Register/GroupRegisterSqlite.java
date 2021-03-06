package edu.cs.sm.GroupLogIn_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs.sm.GroupTasks.GroupMainActivity;
import edu.cs.sm.R;

public class GroupRegisterSqlite extends AppCompatActivity {

    EditText name, id, password, confirmpassword;
    TextView signinPage;
    Button makegroup;
    GDBhelper DB;

    public final static  String NAME = "ID";
    public final static  String PASS = "PASSWORD";
    public final static  String FLAG = "cbFLAG";

    private boolean flag = false;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register_sqlite);
        getSupportActionBar().hide();

        setUpViews();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();


        signinPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupRegisterSqlite.this,
                        GroupLogInSqlite.class);
                startActivity(intent);
            }
        });
    }

    private void setUpViews() {

        name = findViewById(R.id.etFullName);
        id = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        confirmpassword = findViewById(R.id.etConfirmPassword);
        signinPage = findViewById(R.id.btnRegisterLogin);
        makegroup = findViewById(R.id.btnRegister);
        DB = new GDBhelper(this);
    }

    public void CreateGroup(View view) {

        String groupname = name.getText().toString();
        String groupid = id.getText().toString();
        String pass = password.getText().toString();
        String repass = confirmpassword.getText().toString();

        //if any of the edittext is empty a toast will pop up
        if (groupname.equals("") || groupid.equals("") ||
                pass.equals("") || repass.equals("")){

            Toast.makeText(this, "fields should not be empty", Toast.LENGTH_SHORT).show();
        }

        else {
            if (pass.equals(repass)){ // the password field and confirm password field are the same
                Boolean checkid = DB.checkID(groupid); // checks if there is a group registered using this id before
                if (checkid == false){ // there is no user registered with that email
                    Boolean insert = DB.insertData(groupname, groupid, pass); // insert data into the table
                    if (insert == true) { // if it group registered successfully switch to home activity
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GroupRegisterSqlite.this, GroupMainActivity.class);
                        editor.putString(NAME, groupid);
                        //editor.putString(PASS, pass);
                        //editor.putBoolean(FLAG, true);

                        editor.commit();
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Group already exist", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "passwords not matching", Toast.LENGTH_SHORT).show();
        }

    }

}