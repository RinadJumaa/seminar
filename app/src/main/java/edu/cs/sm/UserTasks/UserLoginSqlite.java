package edu.cs.sm.UserTasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.cs.sm.R;
import edu.cs.sm.SecondActivity;

public class UserLoginSqlite extends AppCompatActivity {

    EditText email, password;
    TextView registerPage;
    Button login;
    DBhelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sqlite);

        setUpViews();

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
        DB =new DBhelper(this);
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
                        stringBuffer.append(cursor.getString(0));
                    }
                    String str = stringBuffer.toString();
                    System.out.println(str);
                    intent.putExtra("username", str);
                }

                startActivity(intent);
            }
            else
                Toast.makeText(this, "Check your email or password again", Toast.LENGTH_SHORT).show();
        }

    }
}