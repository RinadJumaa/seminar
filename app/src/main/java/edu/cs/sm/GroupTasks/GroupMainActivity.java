package edu.cs.sm.GroupTasks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs.sm.R;

public class GroupMainActivity extends AppCompatActivity {

    GroupDBHelper mydb;
    LinearLayout empty;
    NestedScrollView scrollView;
    LinearLayout todayContainer;
    GroupNoScrollListView taskListToday;
    ArrayList<HashMap<String, String>> todayList = new ArrayList<HashMap<String, String>>();
//    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<HashMap<String, String>>();
//    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_group_main);

        mydb = new GroupDBHelper(this);
        empty = findViewById(R.id.empty);
        scrollView = findViewById(R.id.scrollView);
        todayContainer = findViewById(R.id.todayContainer);
//        tomorrowContainer = findViewById(R.id.tomorrowContainer);
//        otherContainer = findViewById(R.id.otherContainer);
        taskListToday = findViewById(R.id.taskListToday);
//        taskListTomorrow = findViewById(R.id.taskListTomorrow);
//        taskListUpcoming = findViewById(R.id.taskListUpcoming);
    }

    public void openAddModifyTask(View view) {
        startActivity(new Intent(this, GroupAddModifyTask.class));
    }


    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }


    public void populateData() {
        mydb = new GroupDBHelper(this);

        runOnUiThread(new Runnable() {
            public void run() {
                fetchDataFromDB();
            }
        });
    }


    public void fetchDataFromDB() {
        todayList.clear();
//        tomorrowList.clear();
//        upcomingList.clear();

        Cursor today = mydb.getTodayTask();
//        Cursor tomorrow = mydb.getTomorrowTask();
//        Cursor upcoming = mydb.getUpcomingTask();

        loadDataList(today, todayList);
//        loadDataList(tomorrow, tomorrowList);
//        loadDataList(upcoming, upcomingList);


        if (todayList.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            if (todayList.isEmpty()) {
                todayContainer.setVisibility(View.GONE);
            } else {
                todayContainer.setVisibility(View.VISIBLE);
                loadListView(taskListToday, todayList);
            }

//            if (tomorrowList.isEmpty()) {
//                tomorrowContainer.setVisibility(View.GONE);
//            } else {
//                tomorrowContainer.setVisibility(View.VISIBLE);
//                loadListView(taskListTomorrow, tomorrowList);
//            }
//
//            if (upcomingList.isEmpty()) {
//                otherContainer.setVisibility(View.GONE);
//            } else {
//                otherContainer.setVisibility(View.VISIBLE);
//                loadListView(taskListUpcoming, upcomingList);
//            }
        }
    }


    public void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList) {
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                HashMap<String, String> mapToday = new HashMap<String, String>();
                mapToday.put("id", cursor.getString(0).toString());
                mapToday.put("task", cursor.getString(1).toString());
                mapToday.put("description", cursor.getString(2).toString());
                mapToday.put("location",cursor.getString(3).toString());
                mapToday.put("status", cursor.getString(4).toString());
                dataList.add(mapToday);
                cursor.moveToNext();
            }
        }
    }

    public void loadListView(GroupNoScrollListView listView, final ArrayList<HashMap<String, String>> dataList) {
        GroupListTaskAdapter adapter = new GroupListTaskAdapter(this, dataList, mydb);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(GroupMainActivity.this, GroupAddModifyTask.class);
                i.putExtra("isModify", true);
                i.putExtra("id", dataList.get(+position).get("id"));
                startActivity(i);
            }
        });
    }
}
