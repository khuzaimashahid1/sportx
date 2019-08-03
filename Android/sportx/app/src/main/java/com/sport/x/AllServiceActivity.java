package com.sport.x;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.sport.x.Adapters.CustomerServiceAdapter;
import com.sport.x.Misc.Misc;
import com.sport.x.Models.Service;
import com.sport.x.SharedPref.SharedPref;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllServiceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SearchView.OnQueryTextListener {

    Misc misc;
    SharedPref sharedPref;
    private RecyclerView recyclerView;
    CustomerServiceAdapter customerServiceAdapter;
    private ArrayList<Service> serviceListModel;
    private SearchView searchService = null;
    private Context context;
    private SwipeRefreshLayout refresh;

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
        setContentView(R.layout.activity_all_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Book Services");

        context = this;

        misc = new Misc(this);
        sharedPref = new SharedPref(this);

        serviceListModel = new ArrayList<>();

        refresh = findViewById(R.id.swipe);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        searchService = findViewById(R.id.sv_search);
        recyclerView = findViewById(R.id.customer_services);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        electrician = findViewById(R.id.electricians);
//        electrician.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchService.setOnQueryTextListener(this);
        searchService.setOnClickListener(this);

        if(misc.isConnectedToInternet()){
            getServices();
        }
        else{
            misc.showToast("No Internet Connection");
        }
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void refreshContent() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getServices();
                refresh.setRefreshing(false);
            }
        });
    }

    public void getServices(){

        Ion.with(this)
                .load(misc.ROOT_PATH+"get_serviceCategory")
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if(e != null) {
                            misc.showToast("Please check your connection");
                            return;
                        }
                        else{


                            try {



                                JSONArray jsonArray = new JSONArray(result.getResult());

                                serviceListModel.clear();
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String service_name = jsonObject.getString("name");
                                    String service_id = jsonObject.getString("_id");
                                    String service_image = jsonObject.getString("picture");


                                    serviceListModel.add(new Service(service_id, service_name, service_image));

                                }
                                customerServiceAdapter = new CustomerServiceAdapter(context, serviceListModel);
                                customerServiceAdapter.setTemp(serviceListModel);
                                recyclerView.setAdapter(customerServiceAdapter);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
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

        if (id == R.id.customer_home) {
            Intent home = new Intent(this, AllServiceActivity.class);
            startActivity(home);
            finish();
        } else if (id == R.id.customer_profile) {
            Intent profile = new Intent(this, ProfileActivity.class);
            startActivity(profile);
            finish();
        }
        else if (id == R.id.conversation) {
            Intent conversation = new Intent(this, ConversationActivity.class);
            startActivity(conversation);
            finish();
        }
        else if (id == R.id.update_password) {
            Intent update = new Intent(this, PasswordUpdate.class);
            startActivity(update);
            finish();
        }
        else if (id == R.id.customer_history) {
            Intent job = new Intent(this, JobHistoryActivity.class);
            startActivity(job);
            finish();
        } else if (id == R.id.customer_complaints) {
            Intent complain = new Intent(this, ComplainActivity.class);
            startActivity(complain);
            finish();
        } else if (id == R.id.customer_help) {
            Intent help = new Intent(this, HelpActivity.class);
            help.putExtra("provider", "no");
            startActivity(help);
            finish();
        } else if (id == R.id.customer_logout) {
            sharedPref.clearSession();
            Intent logout = new Intent(this, LoginActivity.class);
            startActivity(logout);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == searchService.getId()) {
            searchService.onActionViewExpanded();
            searchService.setIconified(false);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (customerServiceAdapter != null) {
            customerServiceAdapter.filter(s);
        }
        return true;

    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (customerServiceAdapter != null) {
            customerServiceAdapter.filter(s);
        }
        return true;

    }
}
