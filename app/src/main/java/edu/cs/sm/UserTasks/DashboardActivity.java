package edu.cs.sm.UserTasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.cs.sm.R;
import edu.cs.sm.UserLogIn_Register.UserLoginSqlite;

public class DashboardActivity extends AppCompatActivity {
    //private SessionHandler session;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public final static  String FLAG = "FLAG";
    public final static  String REGFLAG = "REGFLAG";

    public final static  String NAME = "ID";
    public final static  String PASS = "PASSWORD";
    public final static  String cbFLAG = "cbFLAG";

    private boolean flag = false;
    private boolean regflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView welcomeText = findViewById(R.id.welcomeText);
        setupSharedPrefs();
        flag = prefs.getBoolean(FLAG, false); // b default value
        regflag = prefs.getBoolean(REGFLAG, false); // b default value
        if(flag||regflag) {
            //Intent intent = getIntent();
            String username = prefs.getString("username", "");
            String useremail = prefs.getString("useremail", "");


            System.out.println("USER NAMEEEE " + username + " EMAIL " + useremail);


            welcomeText.setText("Your Name is : " + username + "\n" + "Your email is : " + useremail + "\n");
        }

        Button logoutBtn = findViewById(R.id.btnLogout);
        TextView txtback = findViewById(R.id.txtback);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, UserLoginSqlite.class);
                editor.remove(NAME);
                editor.remove(PASS);
                editor.remove(cbFLAG);
                startActivity(i);
                finish();

            }
        });

        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, SecondActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setupSharedPrefs() {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

    }
}
