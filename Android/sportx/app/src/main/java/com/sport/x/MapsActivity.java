package com.sport.x;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sport.x.Misc.Misc;
import com.sport.x.Models.Service_Provider;


import com.sport.x.SharedPref.SharedPref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker myMarker;
    private double current_latitude, current_longitude;
    private double service_lat = 0, service_lon = 0.0;
    private String provider = "no";
    private String service_id, user_image, service_name, user_name, user_rating, vendor_id, customer_id = null;
    private String  service_provider_name, email, address, picture_profile, contact, password, category;
    private double latitude1, longitude1;
    Misc misc;
    SharedPref sharedPref;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private String u_rating, latitude, longitude;
    private Toolbar toolbar;
    private ArrayList<Service_Provider> vendorList = new ArrayList<>();
    private int i, m, index;
    private TextView serviceTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        misc = new Misc(this);
        sharedPref = new SharedPref(this);

        Intent intent = getIntent();
        service_id = intent.getStringExtra("service_id");
        service_name = intent.getStringExtra("service_name");

        serviceTitle = findViewById(R.id.title);
        serviceTitle.setText(service_name + "Service Provider");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

    }

    private void fetchLastLocation() {
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
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    current_latitude = currentLocation.getLatitude();
                    current_longitude = currentLocation.getLongitude();
                    //Toast.makeText(MapsActivity.this,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MapsActivity.this,"No Location recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        mMap.setOnMarkerClickListener(this);

        if(misc.isConnectedToInternet()){
            fetchService_Provider();
        }
        else{
            misc.showToast("No Internet Connection");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
     //   if(marker.equals(myMarker)){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.service_provider_info);

            TextView serviceName = dialog.findViewById(R.id.service);
            serviceName.setText(service_name);

            String provider= marker.getTitle();
            float index1=marker.getZIndex();
            index=(int)index1;

            TextView agree = dialog.findViewById(R.id.sure);
            agree.setText("View Profile of : " + provider);

//            TextView rating = dialog.findViewById(R.id.provider_rating);
//            if(u_rating != null){
//                rating.setText("Rating: " + user_rating+"/"+5);
//            }
//            else {
//                rating.setText("Rating: " + user_rating);
//            }
            final TextView name = dialog.findViewById(R.id.provider_name);
            name.setText("Name: " + provider);

//            ImageView imageView = dialog.findViewById(R.id.provider_image);
//            if(vendorList.get(0).getServiceProviderPicture().isEmpty()) {
//                imageView.setImageResource(R.drawable.serviceicon);
//            }
//            else{
//                Ion.with(this)
//                        .load(vendorList.get(0).getServiceProviderPicture())
//                        .intoImageView(imageView);
//            }

            TextView hire_button = dialog.findViewById(R.id.hire);
            TextView cancel_button = dialog.findViewById(R.id.cancel);

            hire_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //startActivity(new Intent(getApplicationContext(), service_provider_profile_Activity.class));
                    //finish();


                    Intent intent = new Intent(getApplicationContext(), service_provider_profile_Activity.class);
                    intent.putExtra("service_provider_name", vendorList.get(index).getServiceProviderName());
                    intent.putExtra("service_name", service_name);
                    intent.putExtra("service_provider_email", vendorList.get(index).getServiceProviderEmail());
                    intent.putExtra("service_provider_phone_number", vendorList.get(index).getServiceProviderContact());
                    intent.putExtra("service_provider_address", vendorList.get(index).getServiceProviderAddress());
                    intent.putExtra("service_provider_latitude", vendorList.get(index).getUserLat());
                    intent.putExtra("service_provider_longitude", vendorList.get(index).getUserLon());


                    startActivity(intent);
                   // postJob();
                }
            });

            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        //}
        // return false
        return true;
    }

    private void fetchService_Provider(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Finding " + service_name + " Service Providers");
        pd.setCancelable(false);
        pd.show();

        //misc.showToast(misc.ROOT_PATH+"search/serviceProviderByCategory/football");

        Ion.with(this)
                .load(misc.ROOT_PATH+"search/serviceProviderByCategory/"+service_name)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if(e != null) {
                            misc.showToast("Please check your connection");
                            pd.dismiss();
                            return;
                        }
                        else{
                            try {
                                JSONArray jsonArray = new JSONArray(result.getResult());
                                if(jsonArray.length() < 1) {
                                    misc.showToast("No Service Provider of : " + service_name + " Found");
                                    pd.dismiss();
                                    return;
                                }
                                vendorList.clear();
                                for(i = 1; i < jsonArray.length(); i++){

//                                    misc.showToast("For Loop"+result.getResult());

                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                    JSONObject locat  = jsonObject.getJSONObject("location");

                                    latitude1 = locat.getDouble("lat");
                                    longitude1 = locat.getDouble("long");

                                    service_provider_name = jsonObject.getString("name");
                                    email = jsonObject.getString("email");
                                    address = jsonObject.getString("address");
                                    picture_profile = jsonObject.getString("picture_profile");
                                    contact = jsonObject.getString("contact");
                                    password = jsonObject.getString("password");
                                    category = jsonObject.getString("category");

//
                                    //vendorList.add(new Service_Provider(service_provider_name, email, address, picture, contact, password, category, latitude1, longitude1));

                                    if(getDistance(latitude1, longitude1) < 25) {
                                        vendorList.add(new Service_Provider(service_provider_name, email, address, picture_profile, contact, password, category, latitude1, longitude1));
                                    }
                                }
                                if(vendorList.isEmpty()){
                                    misc.showToast("No Nearby " + service_name + " Found. Please visit again");
                                    pd.dismiss();
                                    return;
                                }
                                for(m = 0; m<vendorList.size();m++)
                                {
                                    LatLng serviceLocation = new LatLng(vendorList.get(m).getUserLat(), vendorList.get(m).getUserLon());

                                    myMarker = mMap.addMarker(new MarkerOptions().position(serviceLocation).title(vendorList.get(m).getServiceProviderName()).zIndex(m));

                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(serviceLocation));
                                }

//
                                mMap.setMinZoomPreference(12);
                                pd.dismiss();

                               // double distance = getDistanceFromLatLonInKm(current_latitude, current_longitude, service_lat, service_lon);
                              //  misc.showToast(String.valueOf(getDistance(Double.parseDouble(vendorList.get(0).getUserLat()), Double.parseDouble(vendorList.get(0).getUserLon()))));
                                //fetchRating(vendorList.get(0).getUserId());
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AllServiceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(MapsActivity.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private double getDistance(double lat, double lon){
        Location locationA = new Location("Point A");
        locationA.setLatitude(current_latitude);
        locationA.setLongitude(current_longitude);

        Location locationB = new Location("Point B");
        locationB.setLatitude(lat);
        locationB.setLongitude(lon);

        return locationA.distanceTo(locationB)/1000;   // in km
    }

    public double getDistanceFromLatLonInKm(double startLat, double startLong, double endLat, double endLong) {
        try {
            final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

            double dLat = Math.toRadians((endLat - startLat));
            double dLong = Math.toRadians((endLong - startLong));

            startLat = Math.toRadians(startLat);
            endLat = Math.toRadians(endLat);

            double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS * c; // <-- d
        } catch (Exception ex) {
            ex.printStackTrace();

            return 0;
        }
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
