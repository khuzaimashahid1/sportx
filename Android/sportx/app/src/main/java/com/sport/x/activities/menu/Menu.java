package com.sport.x.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sport.x.activities.serviceProviderActivities.AccountsActivity;
import com.sport.x.activities.serviceProviderActivities.BookingManagementActivity;
import com.sport.x.activities.customerActivities.HomeActivity;
import com.sport.x.activities.customerActivities.CompareActivity;
import com.sport.x.activities.serviceProviderActivities.StreamActivity;
import com.sport.x.activities.sharedActivities.ComplainActivity;
import com.sport.x.activities.sharedActivities.ConversationActivity;
import com.sport.x.activities.sharedActivities.HelpActivity;
import com.sport.x.activities.customerActivities.BookingManagement;
import com.sport.x.activities.sharedActivities.LoginActivity;
import com.sport.x.activities.customerActivities.UpdatePasswordActivity;
import com.sport.x.activities.customerActivities.UpdateProfileActivity;
import com.sport.x.activities.customerActivities.TournamentActivity;
import com.sport.x.R;
import com.sport.x.SharedPref.SharedPref;
import com.sport.x.activities.sharedActivities.ViewStreamActivity;


public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPref sharedPrefMenu;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    protected FrameLayout contentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefMenu=new SharedPref(this);
        super.onCreate(savedInstanceState);
        if(sharedPrefMenu.getUserRole()==1)
        {
            setContentView(R.layout.menu_service_provider);
        }
        else if(sharedPrefMenu.getUserRole()==2)
        {
            setContentView(R.layout.menu_customer);
        }

        toolbar =  findViewById(R.id.toolbar);
        contentFrameLayout=findViewById(R.id.content_frame);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void inflateView(final int layoutResID)
    {
        getLayoutInflater().inflate(layoutResID, contentFrameLayout);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(sharedPrefMenu.getUserRole()==1)
        {
            if (id == R.id.service_profile) {
                Intent profile = new Intent(this, com.sport.x.activities.serviceProviderActivities.UpdateProfileActivity.class);
                startActivity(profile);
                finish();
            }
            else if (id == R.id.service_update_password) {
                Intent profile = new Intent(this, com.sport.x.activities.serviceProviderActivities.UpdatePasswordActivity.class);
                startActivity(profile);
                finish();
            }
            else if (id == R.id.conversation) {
                Intent conversation = new Intent(this, ConversationActivity.class);
                startActivity(conversation);
                finish();
            }
            else if (id == R.id.accounts) {
                Intent accounts = new Intent(this, AccountsActivity.class);
                startActivity(accounts);
                finish();
            }
            else if (id == R.id.livestream) {
                Intent stream = new Intent(this, StreamActivity.class);
                startActivity(stream);
                finish();
            }
            else if (id == R.id.tournament) {
                Intent conversation = new Intent(this, com.sport.x.activities.serviceProviderActivities.TournamentActivity.class);
                startActivity(conversation);
                finish();
            }
            else if (id == R.id.createtournament) {
                Intent conversation = new Intent(this, com.sport.x.activities.serviceProviderActivities.CreateTournamentActivity.class);
                startActivity(conversation);
                finish();
            }
            else if (id == R.id.service_home) {
                Intent home = new Intent(this, com.sport.x.activities.serviceProviderActivities.HomeActivity.class);
                startActivity(home);
                finish();
            } else if (id == R.id.service_jobs) {
                Intent providers = new Intent(this, BookingManagementActivity.class);
                startActivity(providers);
                finish();
            } else if (id == R.id.service_help) {
                Intent help = new Intent(this, HelpActivity.class);
                help.putExtra("provider", "yes");
                startActivity(help);
                finish();
                finish();
            } else if (id == R.id.logout) {
                sharedPrefMenu.clearSession();
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                finish();
            }
            else if (id == R.id.up_password) {
                Intent updatePassword = new Intent(this, com.sport.x.activities.serviceProviderActivities.UpdatePasswordActivity.class);
                startActivity(updatePassword);
                finish();
            }
        }
        else if(sharedPrefMenu.getUserRole()==2)
        {
            if (id == R.id.customer_home) {
                Intent home = new Intent(this, HomeActivity.class);
                startActivity(home);
                finish();
            } else if (id == R.id.customer_profile) {
                Intent profile = new Intent(this, UpdateProfileActivity.class);
                startActivity(profile);
                finish();
            }
            else if (id == R.id.conversation) {
                Intent conversation = new Intent(this, ConversationActivity.class);
                startActivity(conversation);
                finish();
            }
            else if (id == R.id.compare) {
                Intent compare = new Intent(this, CompareActivity.class);
                startActivity(compare);
                finish();
            }
            else if (id == R.id.tournament) {
                Intent tournament = new Intent(this, TournamentActivity.class);
                startActivity(tournament);
                finish();
            }
            else if (id == R.id.viewstream) {
                Intent view = new Intent(this, ViewStreamActivity.class);
                startActivity(view);
                finish();
            }
            else if (id == R.id.update_password) {
                Intent update = new Intent(this, UpdatePasswordActivity.class);
                startActivity(update);
                finish();
            }
            else if (id == R.id.customer_history) {
                Intent job = new Intent(this, BookingManagement.class);
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
                sharedPrefMenu.clearSession();
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                finish();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}