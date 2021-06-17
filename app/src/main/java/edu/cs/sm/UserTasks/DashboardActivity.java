package edu.cs.sm.UserTasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.cs.sm.R;
import edu.cs.sm.UserLogIn_Register.UserLoginSqlite;

public class DashboardActivity extends AppCompatActivity {
    //private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String useremail = intent.getStringExtra("useremail");

        //System.out.println("USER NAMEEEE "+username+" EMAIL "+ useremail);

        TextView welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Your Name is : "+username+"\n"+"Your email is : "+useremail+"\n");

        Button logoutBtn = findViewById(R.id.btnLogout);
        TextView txtback = findViewById(R.id.txtback);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, UserLoginSqlite.class);
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
}
