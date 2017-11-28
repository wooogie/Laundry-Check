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
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


import ch.meienberger.android.SQL.WashorderDataSource;
import ch.meienberger.android.common.activities.ActivityBase;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.common.logger.LogWrapper;


public class MainActivity extends ActivityBase {

    public static final String TAG = "MainActivity";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private WashorderDataSource dataSource;

    public MainActivity (){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch(item.getItemId()) {
            case R.id.action_clothes:
                Log.d(TAG, "Clothes inventory is called.");

                // Create new fragment with the WashorderID as arg and a new transaction
                ClothesinventoryRecyclerViewFragment ClothesInven = new ClothesinventoryRecyclerViewFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.content_fragment, ClothesInven);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;

        }*/
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);



        Log.i(TAG, "Ready");
    }

}
