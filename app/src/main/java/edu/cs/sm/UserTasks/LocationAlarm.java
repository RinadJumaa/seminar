package edu.cs.sm.UserTasks;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import edu.cs.sm.R;

public class LocationAlarm extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;
    ImageView imgBack;
    int count = 0;
    int mycount = 0;
    static Uri alarmsound;
    String locationName = "";
    int id;
    boolean taskCancel = false;

    // added to save the latlng for current location and last searched location
    double startLongitude, startLatitude, endLongitude, endLatitude;
    SupportMapFragment mapFragment;
    Location currentLocation;
    FusedLocationProviderClient client; // to track current location
    private static final int REQUEST_CODE = 101;
    float result[] = new float[10];

    //EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm);

        imgBack=findViewById(R.id.imgback);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationAlarm.this, AddEditNoteActivity.class);
                startActivity(i);
                finish();
            }
        });

        Intent i = getIntent();
        locationName = locationName + i.getStringExtra("location_name");
        id = i.getIntExtra("id",-1);


        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(locationName,
                    "arrived to Destination", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        client = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        getDesieredLocation();

//        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
//            NotificationChannel channel= new NotificationChannel("arrived",
//                    "arrived to Destination", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String []{
                    Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

        Task<Location> task = client.getLastLocation(); // get the last location the device was in (current location)
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null){
                    currentLocation = location;
                    // get the map when everything is ready
                    mapFragment.getMapAsync(LocationAlarm.this);

                }
            }
        });

        alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //refreshMyLocation();
        System.out.println("LOCATION ISSSSSSSS" + locationName);
        if (!locationName.equals("")) {
            refreshMyLocation();
            refreshDistence();
        }
        else {
            refreshMyLocation();
            Toast.makeText(this, "Add new Place", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDesieredLocation() {
        SearchView searchView = findViewById(R.id.location);

        // write it in a thread
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        map.clear(); //clear the previous markers
                        String location = searchView.getQuery().toString();
                        List<Address> addressList = null;
                        if (location != null || !location.equals("")) {
                            //geocoder class helps adding a new marker on a new location
                            Geocoder geocoder = new Geocoder(LocationAlarm.this);
                            try {
                                addressList = geocoder.getFromLocationName(location, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address address = addressList.get(0);

                            endLatitude = address.getLatitude();
                            endLongitude = address.getLongitude();
                            //adding the new marker
                            LatLng latLng = new LatLng(endLatitude, endLongitude);
                            map.addMarker(new MarkerOptions().position(latLng).title(location));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                            //System.out.println("LOCATION IS     "+location);


//                            Toast.makeText(getApplicationContext(),endLatitude+ "---"
//
//                                    + endLongitude, Toast.LENGTH_SHORT).show();


                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
                //I wrote new OnMapReadyCallback() to not get confused with the other method below
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!locationName.equals(""))
                    refreshDistence();
                    refreshMyLocation();
                handler.postDelayed(this,5000);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!locationName.equals(""))
                    refreshDistence();
                refreshMyLocation();
                handler.postDelayed(this,5000);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        startLatitude = currentLocation.getLatitude();
        startLongitude = currentLocation.getLongitude();

        LatLng latLng = new LatLng(startLatitude, startLongitude); // new latlng with the current location
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        googleMap.addMarker(markerOptions); //add the marker
        //map = googleMap;
    }



    public void CalculateDistance(View view) {

        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);

        Toast.makeText(getApplicationContext(), "you are: " + result[0] + " m away from " + locationName, Toast.LENGTH_SHORT).show();

    }

    public void refreshDistence(){

        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(LocationAlarm.this);
        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        endLatitude = address.getLatitude();
        endLongitude = address.getLongitude();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                    Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);
                    //Toast.makeText(getApplicationContext(), "Distance: " + result[0], Toast.LENGTH_SHORT).show();
                    System.out.println("refreshed   " + result[0]);
                    if (result[0] < 2000000 )
                    Toast.makeText(LocationAlarm.this, "you are: " + result[0]
                            + " m away from "+locationName,
                            Toast.LENGTH_SHORT).show();
                    generateNotification();
                handler.postDelayed(this,5000);
            }
        });
        count++;
//        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);
//
//        //Toast.makeText(getApplicationContext(), "Distance: " + result[0], Toast.LENGTH_SHORT).show();
//        System.out.println("refreshed   " + result[0]);
//
//        generateNotification();

        //refresh(5000);

    }

    public void My_Location(View view) {

        // this if statement checks if permission to access location is accepted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String []{
                    Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation(); // get the last location the device was in (current location)
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null){
                    currentLocation = location;

                    //show the latitude and longitude for the current location and display it as toast
//                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()
//                            + "---" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//
                    // get the map when everything is ready
                    mapFragment.getMapAsync(LocationAlarm.this);

                }
            }
        });
    }

    public void refreshMyLocation(){
        //map.clear();
        mycount++;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                    if (ActivityCompat.checkSelfPermission(LocationAlarm.this,Manifest.permission.ACCESS_FINE_LOCATION)!=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationAlarm.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(LocationAlarm.this, new String []{
                                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
                        return;
                    }


                    Task<Location> task = client.getLastLocation(); // get the last location the device was in (current location)
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(final Location location) {
                            if (location != null){
                                currentLocation = location;
                                // get the map when everything is ready

                                LatLng latLng = new LatLng(startLatitude, startLongitude);
//                                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                                    @Override
//                                    public void onMapReady(GoogleMap googleMap) {
//                                        map = googleMap;
//                                    }
//                                });
                                //LatLng latLng = new LatLng(startLatitude, startLongitude); // new latlng with the current location
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");

                            }
                        }
                    });

                    System.out.println("refreshed  " + startLatitude+ " -- " + startLongitude );
                    //Toast.makeText(LocationAlarm.this, "your location " + startLatitude+ " -- " + startLongitude, Toast.LENGTH_SHORT).show();
                    generateNotification();
                handler.postDelayed(this,5000);
            }
        });


        //refresh(5000);

    }

//    private void refresh(int milliseconds) {
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                refreshMyLocation();
//                refreshDistence();
//            }
//        };
//        handler.postDelayed(runnable, milliseconds);
//
//    }

    public void AddLocation(View view) {
        SearchView searchView = findViewById(R.id.location);
        String location = searchView.getQuery().toString();
        Intent intent = new Intent();
        intent.putExtra("locationName",location);
        setResult(RESULT_OK,intent);
//        startActivity(intent);
        finish();

//        if(result[0] <= 5000 && result[0]!= 0){
//            generateNotification();
//        }

    }

    public void generateNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                LocationAlarm.this, locationName);
        if(result[0] <= 500 && result[0]!= 0) {
            if (locationName.length() > 0) {

                builder.setContentTitle("Reminder");
                builder.setContentText("you are less than 5000m don't forget the task " + locationName);
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setSound(alarmsound);
                builder.setAutoCancel(false);
                builder.setOngoing(true);
//            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(LocationAlarm.this);
//            managerCompat.notify(id, builder.build());
            }
            else{
            builder.setAutoCancel(false);
            builder.setOngoing(true);
            }
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(LocationAlarm.this);
        managerCompat.notify(id, builder.build());
        }
    }

     @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    refreshMyLocation();
                }
                break;
        }
    }


}