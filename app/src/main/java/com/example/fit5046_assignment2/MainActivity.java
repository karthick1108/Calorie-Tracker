package com.example.fit5046_assignment2;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //public static int flag=0;
    private String message;
    private String userId;
    private String address;
    private String postcode;
    private String usrsteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        message = getIntent().getStringExtra("firstname");
        userId = getIntent().getStringExtra("userid");
        address = getIntent().getStringExtra("usraddress");
        postcode = getIntent().getStringExtra("usrpostcode");
        usrsteps = getIntent().getStringExtra("usrsteps");



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Calorie Tracker");
        Fragment home = new HomeScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString("firstname",message);
        bundle.putString("userid",userId);
        bundle.putString("usraddress",address);
        bundle.putString("usrpostcode",postcode);
        bundle.putString("usrsteps",usrsteps);
        home.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,home).commit();
        callLoginActivity();
    }
    private void callLoginActivity()
    {
        if(!getIntent().getBooleanExtra("isFromLogin",false))
        {
            Intent indent = new Intent(MainActivity.this, LoginActivity.class);
            indent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            indent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            indent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            indent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            indent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(indent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;
        switch (id) {
            case R.id.nav_dashboard:
                Bundle bundle = new Bundle();
                bundle.putString("firstname",message);
                nextFragment = new HomeScreenFragment();
                nextFragment.setArguments(bundle);
                break;
            case R.id.nav_diet:
                nextFragment = new MyDailyDietScreenFragment();
                break;
            case R.id.nav_steps:
                Bundle stepBundle = new Bundle();
                stepBundle.putString("userid",userId);
                nextFragment = new StepsFragment();
                nextFragment.setArguments(stepBundle);
                break;
            case R.id.nav_caltracker:
                Bundle calBundle = new Bundle();
                calBundle.putString("userid",userId);
                calBundle.putString("usrsteps",usrsteps);
                nextFragment = new CalorieTrackerScreenFragment();
                nextFragment.setArguments(calBundle);
                break;
            case R.id.nav_report:
                nextFragment = new ReportFragment();
                break;
            case R.id.nav_map:
                Bundle mapBundle = new Bundle();
                mapBundle.putString("usraddress",address);;
                mapBundle.putString("usrpostcode",postcode);
                nextFragment = new MapScreenFragment();
                nextFragment.setArguments(mapBundle);
                break;

        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                nextFragment).commit();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
}
