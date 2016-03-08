package com.medikeen.pharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.medikeen.pharmacy.bean.PrescriptionBean;
import com.medikeen.pharmacy.fragments.AboutUsFragment;
import com.medikeen.pharmacy.fragments.HistoryFragment;
import com.medikeen.pharmacy.fragments.HomeFragment;
import com.medikeen.pharmacy.fragments.SettingsFragment;
import com.medikeen.pharmacy.utils.SessionManager;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    FloatingActionButton fab;

    InputStream inputStream;
    StringBuilder stringBuilder;
    String jsonResponseString;

    ProgressDialog progressDialog;

    SessionManager sessionManager;

    String sessionId;

    public static ArrayList<PrescriptionBean> prescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sessionManager = new SessionManager(MainActivity.this);

        sessionId = sessionManager.getUserDetails().getPharmacyUserSessionId();

        prescriptionList = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SelectItem(0);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_home) {
            SelectItem(0);
        } else if (id == R.id.nav_history) {
            SelectItem(1);
        } else if (id == R.id.nav_settings) {
            SelectItem(2);
        } else if (id == R.id.nav_about_us) {
            SelectItem(3);
        } else if (id == R.id.nav_log_out) {
            SelectItem(4);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideFAB(boolean hide) {
        if (hide) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    public void SelectItem(int position) {

        switch (position) {
            case 0:
                fragment = new HomeFragment();

                android.support.v4.app.FragmentTransaction ftHome = getSupportFragmentManager()
                        .beginTransaction();
                ftHome.replace(R.id.frame_container, fragment);
                ftHome.commit();

                hideFAB(true);

                break;
            case 1:
                fragment = new HistoryFragment();

                android.support.v4.app.FragmentTransaction ftFeedback = getSupportFragmentManager()
                        .beginTransaction();
                ftFeedback.replace(R.id.frame_container, fragment);
                ftFeedback.commit();

                hideFAB(true);

                break;
            case 2:
                fragment = new SettingsFragment();

                android.support.v4.app.FragmentTransaction ftTandC = getSupportFragmentManager()
                        .beginTransaction();
                ftTandC.replace(R.id.frame_container, fragment);
                ftTandC.commit();

                hideFAB(true);

                break;
            case 3:
                fragment = new AboutUsFragment();

                android.support.v4.app.FragmentTransaction ftAboutUs = getSupportFragmentManager()
                        .beginTransaction();
                ftAboutUs.replace(R.id.frame_container, fragment);
                ftAboutUs.commit();

                hideFAB(true);

                break;
            case 4:
                sessionManager.logoutUser();

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

                break;
            default:
                break;
        }
    }
}
