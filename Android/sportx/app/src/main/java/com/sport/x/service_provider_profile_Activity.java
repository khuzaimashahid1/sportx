package com.sport.x;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.sport.x.AdminActivities.AllJobsActivity;
import com.sport.x.Misc.Misc;
import com.sport.x.SharedPref.SharedPref;

import static android.view.View.GONE;

public class service_provider_profile_Activity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Marker myMarker;
    private TextView name,email, service, phone, address;
    private Location currentLocation;
    private double service_provider_longitude, service_provider_latitude;
    private String phoneNumber, service_name, service_provider_email,service_provider_name, service_provider_address, service_provider_phone_number;
    private Button book, msg, call;
    private EditText meesage;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    SharedPref sharedPref;
    Misc misc;
    private boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);
        setTitle("Service Provider Profile");

        misc = new Misc(this);
        sharedPref = new SharedPref(this);


        name = findViewById(R.id.c_name);
        email = findViewById(R.id.c_email);
        service = findViewById(R.id.c_service);
        phone = findViewById(R.id.c_phone);
        address= findViewById(R.id.c_address);
        call = findViewById(R.id.make_call);
        msg = findViewById(R.id.message);
        msg.setOnClickListener(this);

        book = findViewById(R.id.book);
        book.setOnClickListener(this);

        call.setOnClickListener(this);

//        String id = sharedPref.getUserId();
//        if(id == null) {
//            complete.setVisibility(GONE);
//        }

        Intent intent = getIntent();

        service_name=intent.getStringExtra("service_name");
        service_provider_email=intent.getStringExtra("service_provider_email");
        service_provider_name=intent.getStringExtra("service_provider_name");
        service_provider_address=intent.getStringExtra("service_provider_address");
        service_provider_phone_number = intent.getStringExtra("service_provider_phone_number");
        service_provider_latitude=intent.getDoubleExtra("service_provider_latitude",33);
        service_provider_longitude=intent.getDoubleExtra("service_provider_longitude",73);



        name.setText("Name: " + service_provider_name);
        email.setText("Email: " + service_provider_email);
        service.setText("Service: " + service_name);
        phone.setText("Phone: " + service_provider_phone_number);
        address.setText("Address: " + service_provider_address);





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ip_map);
        mapFragment.getMapAsync(service_provider_profile_Activity.this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(service_provider_latitude, service_provider_longitude);
        mMap.setMinZoomPreference(15);
        myMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Service Provider Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBackPressed() {
//        if(sharedPref.getUserId() == null) {
//            Intent intent = new Intent(this, MapsActivity.class);
//            intent.putExtra("service_name",service_name);
//            startActivity(intent);
//            finish();
//        }
//        else{
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("service_name",service_name);
            startActivity(intent);
            finish();
//        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == book.getId()) {
            Book();
        }
        if(v.getId() == call.getId()){
            makeCall();
        }
        if(v.getId() == msg.getId()){
            sendSMS();
        }
    }

//    public void Book(View v)
//    {
//        Intent intent5 = new Intent(this, BookingActivity.class);
//        intent5.putExtra("service_name",service_name);
//        intent5.putExtra("service_email",email.getText().toString());
//        startActivity(intent5
//        );
//        finish();
//    }
    private void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + service_provider_phone_number));
        startActivity(intent);
    }

    private void sendSMS(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address", service_provider_phone_number);
        sendIntent.putExtra("sms_body", "Hi there!");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }

    private void Book(){
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("service_provider_name",service_provider_name);
        intent.putExtra("service_name", service_name);
        intent.putExtra("service_provider_email", service_provider_email);
        intent.putExtra("service_provider_phone_number", service_provider_phone_number);
        intent.putExtra("service_provider_address", service_provider_address);
        intent.putExtra("service_provider_latitude", service_provider_latitude);
        intent.putExtra("service_provider_longitude", service_provider_longitude);
        startActivity(intent);
    }



}