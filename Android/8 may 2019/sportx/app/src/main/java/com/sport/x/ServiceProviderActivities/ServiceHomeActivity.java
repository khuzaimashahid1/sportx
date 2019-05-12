package com.sport.x.ServiceProviderActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.sport.x.HelpActivity;
import com.sport.x.LoginActivity;
import com.sport.x.Misc.Misc;
import com.sport.x.R;
import com.sport.x.SharedPref.SharedPref;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceHomeActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {


    private GoogleMap mMap;
    private Marker myMarker;
    private double current_latitude, current_longitude;
    private TextView txt1, txt2;
    private CircularImageView img1;
    Misc misc;
    SharedPref sharedPref;
    private String password;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");

        sharedPref = new SharedPref(this);
        misc = new Misc(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
       // txt1 = headerView.findViewById(R.id.textView);
        txt2 = headerView.findViewById(R.id.textView1);
      //  img1 = headerView.findViewById(R.id.imageView);
        navigationView.setNavigationItemSelectedListener(this);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        syncMap();
    }

    private void syncMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.service_map);
        mapFragment.getMapAsync(ServiceHomeActivity.this);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.service_profile) {
            Intent profile = new Intent(this, ServiceProfileActivity.class);
            startActivity(profile);
            finish();
        } else if (id == R.id.service_home) {
            Intent home = new Intent(this, ServiceHomeActivity.class);
            startActivity(home);
            finish();
        } else if (id == R.id.service_providers) {
            Intent providers = new Intent(this, AllProvidersActivity.class);
            startActivity(providers);
            finish();
        }else if (id == R.id.service_jobs) {
            Intent providers = new Intent(this, ProviderJobsActivity.class);
            startActivity(providers);
            finish();
        }else if (id == R.id.service_update) {
            Intent update = new Intent(this, UpdateServicesActivity.class);
            startActivity(update);
            finish();
        } else if (id == R.id.service_help) {
            Intent help = new Intent(this, HelpActivity.class);
            help.putExtra("provider", "yes");
            startActivity(help);
            finish();
            finish();
        } else if (id == R.id.logout) {
            sharedPref.clearSession();
            Intent logout = new Intent(this, LoginActivity.class);
            startActivity(logout);
            finish();
        }
        else if (id == R.id.up_password) {
            Intent updatePassword = new Intent(this, SerivceUpdatePasswordActivity.class);
            updatePassword.putExtra("password", password);
            startActivity(updatePassword);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        if(misc.isConnectedToInternet()) {
            fetchUserProfile();
        }
        else{
            misc.showToast("No Internet Connection");
        }
    }

    private void fetchUserProfile(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        String id = sharedPref.getUserId();
        Ion.with(this)
                .load(misc.ROOT_PATH+"user_profile/"+id)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if(e != null) {
                            pd.dismiss();
                            misc.showToast("Internet Connection Problem");
                            return;
                        }
                        else{
                            try {
                                pd.dismiss();
                                JSONObject jsonObject = new JSONObject(result.getResult());
                                current_latitude = Double.parseDouble(jsonObject.getString("user_lat"));
                                current_longitude = Double.parseDouble(jsonObject.getString("user_lon"));
                                String user_image = jsonObject.getString("user_image");

                                if(user_image.isEmpty()){
                                   // img1.setImageResource(R.drawable.serviceicon);
                                }
                                else{
                                  //  Ion.with(getApplicationContext()).load(jsonObject.getString("user_image").replace("\"","")).intoImageView(img1);
                                }

                             //   txt1.setText(jsonObject.getString("user_name"));
                              //  txt2.setText(jsonObject.getString("user_email"));
                                password = jsonObject.getString("user_password");
                                LatLng service = new LatLng(current_latitude, current_longitude);
                                mMap.setMinZoomPreference(15);
                                myMarker = mMap.addMarker(new MarkerOptions().position(service).title("Service Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(service));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }
}
