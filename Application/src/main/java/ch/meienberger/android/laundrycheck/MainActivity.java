/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package ch.meienberger.android.laundrycheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;



import ch.meienberger.android.SQL.WashorderDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.common.logger.LogWrapper;
import ch.meienberger.android.laundrycheck.Fragments.ClothesinventoryRecyclerViewFragment;
import ch.meienberger.android.laundrycheck.Fragments.SettingsViewFragment;
import ch.meienberger.android.laundrycheck.Fragments.WashorderRecyclerViewFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "MainActivity";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private WashorderDataSource dataSource;

    public MainActivity (){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Start logging
        initializeLogging();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //init SQL
        dataSource = new WashorderDataSource(this);


        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            WashorderRecyclerViewFragment washorderfragment = new WashorderRecyclerViewFragment();
            transaction.replace(R.id.content_fragment, washorderfragment);
            transaction.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "Naviagation is called: " + item.getItemId() );

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch(item.getItemId()) {
            case R.id.action_clothes:
                Log.d(TAG, "Clothes inventory is called.");
                setTitle(R.string.action_clothes);

                // Create new fragment
                ClothesinventoryRecyclerViewFragment clothesInven = new ClothesinventoryRecyclerViewFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.content_fragment, clothesInven);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;

            case R.id.action_washorder:
                Log.d(TAG, "Washorder is called.");
                setTitle(R.string.action_washorder);


                // Create new fragment
                WashorderRecyclerViewFragment washorder = new WashorderRecyclerViewFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.content_fragment, washorder);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;

            case R.id.action_settings:
                Log.d(TAG, "Settings is called.");
setTitle("");

                // Create new fragment
                SettingsViewFragment settings = new SettingsViewFragment();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.content_fragment, settings);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;

        }

        //Close Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

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

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }

}
