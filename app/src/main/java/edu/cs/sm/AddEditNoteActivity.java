package edu.cs.sm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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

import edu.cs.sm.LocationAlarm;
import edu.cs.sm.R;


public class AddEditNoteActivity extends AppCompatActivity {

    Dialog myDialog;
    EditText title;
    EditText description;
    NumberPicker numberPicker;
    private static final String TAG = "MainActivity";
    private TextView mDisplayDate;
    private ImageView imgcalender;
    private TextView txtRepeat;
    private TextView locationName;
    Button btnAddlocation;
    private Switch repeat_switch;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        setUPviews();

        Intent checkboxIntent = getIntent();
        String checkBox = checkboxIntent.getStringExtra("cbvalue");

        btnAddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note_title = title.getText().toString();
                Intent i = new Intent(AddEditNoteActivity.this, LocationAlarm.class);
                i.putExtra("note_title", note_title);
                int id = getIntent().getIntExtra("id",-1);
                if (id > -1){
                    i.putExtra("id",id);
                }

                String switchValue;
                if(repeat_switch.isChecked())
                    switchValue = "true";
                else
                    switchValue= "false";
                i.putExtra("switchValue",switchValue);



                //i.putExtra("cbvalue",checkBox);
                startActivityForResult(i,1);
                //finish();
            }
        });


//        Intent loc = getIntent();
//        String location = loc.getStringExtra("locationName");
//        locationName.setText(location);

        imgcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddEditNoteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        numberPicker = findViewById(R.id.addNote_numberPacker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            setTitle("Edit Note");
            title.setText(intent.getStringExtra("title"));
            description.setText(intent.getStringExtra("description"));
            numberPicker.setValue(intent.getIntExtra("priority", 1));
        } else {
            setTitle("Add Note");
        }

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
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        imgcalender=findViewById(R.id.imgcalender);
        btnAddlocation=findViewById(R.id.btnmap);
        locationName = findViewById(R.id.locationName);
        repeat_switch = findViewById(R.id.repeat_switch);
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


    private void saveNote() {
        String note_title = title.getText().toString();
        String note_description = description.getText().toString();
        String location = locationName.getText().toString();
        int note_priority = numberPicker.getValue();

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