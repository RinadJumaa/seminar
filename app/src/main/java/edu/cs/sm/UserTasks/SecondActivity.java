package edu.cs.sm.UserTasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.cs.sm.GroupLogIn_Register.GroupLogInSqlite;
import edu.cs.sm.R;


public class SecondActivity extends AppCompatActivity {
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    public final static  String NAME = "ID";
    public final static  String PASS = "PASSWORD";
    public final static  String FLAG = "cbFLAG";
    private TextView etUserinfo;
    private TextView etMyday;
    private TextView etUMakegroup;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        setContentView(R.layout.activity_second);
        etUserinfo = findViewById(R.id.btninfo);
        etMyday = findViewById(R.id.btnMyday);
        etUMakegroup = findViewById(R.id.btnMakeGroup);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String useremail = intent.getStringExtra("useremail");

        etUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, DashboardActivity.class);

                i.putExtra("username",username);
                i.putExtra("useremail",useremail);

                startActivity(i);
                finish();
            }
        });


        //Launch Registration screen when Register Button is clicked
        etMyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, NoteActivity.class);
                startActivity(i);
                finish();
            }
        });
        etUMakegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecondActivity.this, GroupLogInSqlite.class);
//                editor.remove(NAME);
//                editor.remove(PASS);
                editor.putBoolean(FLAG,false);
                startActivity(i);
                finish();
            }
        });




    }
}