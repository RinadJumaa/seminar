package edu.cs.sm.GroupTasks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.cs.sm.R;

public class GroupAddModifyTask extends AppCompatActivity {

   // Calendar calendar;
    GroupDBHelper mydb;
    Boolean isModify = false;
    String task_id;
    TextView toolbar_title;
    EditText title,description;
    TextView dateText;
    Button save_btn,btnlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_group_add_modify_task);

        mydb = new GroupDBHelper(getApplicationContext());
       // calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        title = findViewById(R.id.edttitle);
        description = findViewById(R.id.edtdescription);
        //dateText = findViewById(R.id.dateText);
        save_btn = findViewById(R.id.save_btn);
        btnlocation=findViewById(R.id.btnlocation);

        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupAddModifyTask.this, GroupLocation.class);
                startActivity(i);
                finish();
            }
        });
        //dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));


        Intent intent = getIntent();
        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false);
            task_id = intent.getStringExtra("id");
            init_modify();
        }


    }

    public void init_modify() {
      //  toolbar_title.setText("Modify Task");
        save_btn.setText("Update");
        ImageView deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setVisibility(View.VISIBLE);
        Cursor task = mydb.getSingleTask(task_id);
        if (task != null) {
            task.moveToFirst();

            title.setText(task.getString(1));
            description.setText(task.getString(2));

//            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                calendar.setTime(iso8601Format.parse(task.getString(2)));
//            } catch (ParseException e) {
//            }
//
//            dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));


        }

    }


    public void saveTask(View v) {

        /*Checking for Empty Task*/
        if (title.getText().toString().trim().length() > 0) {

            if (isModify) {
                mydb.updateTask(task_id, title.getText().toString(), description.getText().toString());
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertTask(title.getText().toString(), description.getText().toString());
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Empty task can't be saved.", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteTask(View v) {
        mydb.deleteTask(task_id);
        Toast.makeText(getApplicationContext(), "Task Removed", Toast.LENGTH_SHORT).show();
        finish();
    }


//    public void chooseDate(View view) {
//        final View dialogView = View.inflate(this, R.layout.group_date_picker, null);
//        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
//        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        builder.setTitle("Choose Date");
//        builder.setNegativeButton("Cancel", null);
//        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//
//                calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
//                dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));
//
//            }
//        });
//        builder.show();
//    }
}