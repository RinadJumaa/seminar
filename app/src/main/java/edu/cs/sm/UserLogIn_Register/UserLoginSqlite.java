package edu.cs.sm.UserLogIn_Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs.sm.R;
import edu.cs.sm.UserTasks.SecondActivity;

public class UserLoginSqlite extends AppCompatActivity {

    EditText email, password;
    TextView registerPage;
    CheckBox checkBox;
    Button login;
    DBhelper DB;

    public final static  String NAME = "NAME";
    public final static  String PASS = "PASS";
    public final static  String FLAG = "FLAG";

    private boolean flag = false;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sqlite);
        getSupportActionBar().hide();
        setUpViews();
        setupSharedPrefs();
        CheckPrefs();

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginSqlite.this,
                        UserRegisterSqlite.class);
                startActivity(intent);
            }
        });

    }

    private void setUpViews() {

        email = findViewById(R.id.etLoginUsername);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.btnLogin);
        registerPage = findViewById(R.id.btnLoginRegister);
        checkBox = findViewById(R.id.checkbox);
        DB =new DBhelper(this);
    }

    private void setupSharedPrefs() {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

    }

    private void CheckPrefs() {

        flag = prefs.getBoolean(FLAG, false); // b default value

        if(flag){

            String useremail = prefs.getString(NAME, "");
            String userpass = prefs.getString(PASS, "");

            email.setText(useremail);
            password.setText(userpass);

            checkBox.setChecked(true);

        }

    }

    public void LogInwithUser(View view) {

        String emailstr = email.getText().toString();
        String pass = password.getText().toString();



        //if any of the edittext is empty a toast will pop up
        if (emailstr.equals("") || pass.equals("")){
            Toast.makeText(this, "fields should not be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            Boolean checkPassword = DB.checkemailPassword(emailstr,pass); //checks if there is a user is registered and the password is correct
            if (checkPassword == true){ //if the password and email is correct switch to home page
                Toast.makeText(this, "Signing in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserLoginSqlite.this, SecondActivity.class);

                Cursor cursor = DB.getUserName(emailstr);
                if (cursor.getCount() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        stringBuffer.append(cursor.getString(0)+ "\n");
                        stringBuffer.append(cursor.getString(1));
                    }
                    String [] userdata = stringBuffer.toString().split("\n");
                    String name = userdata[0];
                    String useremail = userdata[1];
                    System.out.println(name);
                    System.out.println(useremail);
                    intent.putExtra("username", name);
                    intent.putExtra("useremail", useremail);
                }

                if(checkBox.isChecked()){
                    if(!flag){

                        editor.putString(NAME, emailstr);
                        editor.putString(PASS, pass);
                        editor.putBoolean(FLAG, true);

                        editor.commit();

                    }
                }

                startActivity(intent);
            }
            else
                Toast.makeText(this, "Check your email or password again", Toast.LENGTH_SHORT).show();
        }

    }
}