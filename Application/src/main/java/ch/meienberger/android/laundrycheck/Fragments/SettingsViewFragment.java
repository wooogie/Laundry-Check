package ch.meienberger.android.laundrycheck.Fragments;

/**
 * This Fragments holdes all settings for the app and saves it to the shared preferences.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import ch.meienberger.android.SQL.ClothesDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;

public class SettingsViewFragment extends Fragment {

    private static final String TAG = "SettingsViewFragment";
    static String PREF_USE_RFID = "pref_use_rfid";
    protected CheckBox mCheckBox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        rootView.setTag(TAG);

        Activity curActivity =  ((Activity) getContext());
        curActivity.setTitle(R.string.action_settings);

        //get shared preferences from main activity
        final SharedPreferences sharedpreferences = curActivity.getPreferences(Context.MODE_PRIVATE);

        // BEGIN_INCLUDE
        mCheckBox = (CheckBox) rootView.findViewById(R.id.checkbox_use_rfid);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saves Settings
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(PREF_USE_RFID,mCheckBox.isChecked());
                editor.commit();
            }
        });

        // END_INCLUDE



        //Fill Fields
        mCheckBox.setChecked(sharedpreferences.getBoolean(PREF_USE_RFID,true));

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * Lifecycle methods
     */
    @Override
    public void onResume() {
        super.onResume();
        Activity curActivity =  ((Activity) getContext());
        curActivity.setTitle(R.string.action_settings);

    }

    @Override
    public void onPause() {


        super.onPause();

    }







}
