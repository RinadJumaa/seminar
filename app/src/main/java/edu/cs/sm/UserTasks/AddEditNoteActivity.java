package edu.cs.sm.UserTasks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import edu.cs.sm.GroupTasks.GroupDBHelper;
import edu.cs.sm.R;


public class AddEditNoteActivity extends AppCompatActivity {

    Dialog myDialog;
    EditText title;
    EditText description;
    NumberPicker numberPicker;
    private static final String TAG = "MainActivity";
    private TextView locationName;
    Button btnAddlocation;
    //private Switch repeat_switch;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        setUPviews();

//        Intent checkboxIntent = getIntent();
//        String checkBox = checkboxIntent.getStringExtra("cbvalue");




        btnAddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location_name = locationName.getText().toString();
                //String notetitle = title.getText().
                if (location_name.equals("Location")||location_name.equals("No place added")){
                    location_name ="";
                }
                else {
                    Intent i = new Intent(AddEditNoteActivity.this, LocationAlarm.class);
                    i.putExtra("location_name", location_name);
                    i.putExtra("title", title.getText().toString());
                    int id = getIntent().getIntExtra("id", -1);
                    if (id > -1) {
                        i.putExtra("id", id);
                    }


                    startActivityForResult(i, 1);
                }
                //finish();
            }
        });


//        Intent loc = getIntent();
//        String location = loc.getStringExtra("locationName");
//        locationName.setText(location);

        numberPicker = findViewById(R.id.addNote_numberPacker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Edit Note");
            title.setText(intent.getStringExtra("title"));
            description.setText(intent.getStringExtra("description"));
            numberPicker.setValue(intent.getIntExtra("priority", 1));
            locationName.setText(intent.getStringExtra("location"));
        } else {
            setTitle("Add Note");
        }

        String buttonText = locationName.getText().toString();
        if (buttonText.equals("")||buttonText.equals("Location")||buttonText.equals("No place added")){
            btnAddlocation.setText("Set Location");
        }
        else
            btnAddlocation.setText("Track Location");


//        txtRepeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView txtclose;
//                Button btnSet;
//                myDialog.setContentView(R.layout.custompopup);
//                btnSet = (Button) myDialog.findViewById(R.id.btnSet);
//                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        myDialog.dismiss();
//                    }
//                });
//
//                btnSet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        RepeatAlarm();
//                    }
//                });
//                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                myDialog.show();
//            }
//        });
    }

    private void setUPviews() {
        myDialog = new Dialog(this);
        title = findViewById(R.id.addNoteActivity_title);
        description = findViewById(R.id.addNoteActivity_description);
        btnAddlocation=findViewById(R.id.btnmap);
        locationName = findViewById(R.id.locationName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            saveNote();
        }
        return true;
    }


    private void RepeatAlarm(){

        Toast.makeText(this, "BUTTON BEEN SET", Toast.LENGTH_SHORT).show();

    }


//    public void init_modify() {
//        //  toolbar_title.setText("Modify Task");
//        save_btn.setText("Update");
//        ImageView deleteTask = findViewById(R.id.deleteTask);
//        deleteTask.setVisibility(View.VISIBLE);
//        Cursor task = mydb.getSingleTask(task_id);
//        if (task != null) {
//            task.moveToFirst();
//
//            title.setText(task.getString(1));
//            description.setText(task.getString(2));
//        }
//    }


    private void saveNote() {
        String note_title = title.getText().toString();
        String note_description = description.getText().toString();
        String location = locationName.getText().toString();
        int note_priority = numberPicker.getValue();

        if(location.length()>0){
        Intent intent = new Intent();
        intent.putExtra("note_title", note_title);
        intent.putExtra("note_description", note_description);
        intent.putExtra("note_priority", note_priority);
        intent.putExtra("location_name",location);

        int id = getIntent().getIntExtra("id",-1);
        if (id > -1){
            intent.putExtra("id",id);
        }
        setResult(RESULT_OK, intent);
        finish();
        } else {
            Toast.makeText(getApplicationContext(), "Add a Location.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //String location = locationName.getText().toString();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("locationName");
                locationName.setText("" + result);
            }
            if (resultCode == RESULT_CANCELED) {
                locationName.setText("No place added");
            }
        }
    }

}