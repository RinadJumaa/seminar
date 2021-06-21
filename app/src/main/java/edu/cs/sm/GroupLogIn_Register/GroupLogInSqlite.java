package edu.cs.sm.GroupLogIn_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs.sm.GroupTasks.GroupMainActivity;
import edu.cs.sm.R;
import edu.cs.sm.UserTasks.SecondActivity;

public class GroupLogInSqlite extends AppCompatActivity {

    EditText groupid, password;
    TextView registerPage, mainpage;
    CheckBox checkBox;
    Button login;
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
        setContentView(R.layout.activity_group_log_in_sqlite);
        getSupportActionBar().hide();
        setUpViews();
        setupSharedPrefs();
        CheckPrefs();

        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupLogInSqlite.this,
                        GroupRegisterSqlite.class);
                startActivity(intent);
            }
        });

    }

    private void setUpViews() {

        groupid = findViewById(R.id.etLoginUsername);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.btnLogin);
        registerPage = findViewById(R.id.btnLoginRegister);
        checkBox = findViewById(R.id.rememberme);
        mainpage = findViewById(R.id.btnback);
        DB =new GDBhelper(this);
    }

    private void setupSharedPrefs() {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

    }

    private void CheckPrefs() {

        flag = prefs.getBoolean(FLAG, false); // b default value

        if(flag){

            String id = prefs.getString(NAME, "");
            String pass = prefs.getString(PASS, "");

            groupid.setText(id);
            password.setText(pass);
            checkBox.setChecked(true);

        }

    }

    public void JoinGroup(View view) {

        String id = groupid.getText().toString();
        String pass = password.getText().toString();

        //if any of the edittext is empty a toast will pop up
        if (id.equals("") || pass.equals("")){
            Toast.makeText(this, "fields should not be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            Boolean checkPassword = DB.checkidPassword(id,pass); //checks if there is a user is registered and the password is correct
            if (checkPassword == true){ //if the password and email is correct switch to home page
                Toast.makeText(this, "Signing in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GroupLogInSqlite.this, GroupMainActivity.class);

//                Cursor cursor = DB.get(emailstr);
//                if (cursor.getCount() > 0) {
//                    StringBuffer stringBuffer = new StringBuffer();
//                    while (cursor.moveToNext()) {
//                        stringBuffer.append(cursor.getString(0));
//                    }
//                    String str = stringBuffer.toString();
//                    System.out.println(str);
//                    intent.putExtra("username", str);
//                }

                if(checkBox.isChecked()){
                    if(!flag){

                        editor.putString(NAME, id);
                        editor.putString(PASS, pass);
                        editor.putBoolean(FLAG, true);

                        editor.commit();

                    }
                }
                intent.putExtra("group_id",id);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "Check your email or password again", Toast.LENGTH_SHORT).show();
        }

    }

}