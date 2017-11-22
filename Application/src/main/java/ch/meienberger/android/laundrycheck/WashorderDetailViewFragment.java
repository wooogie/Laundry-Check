package ch.meienberger.android.laundrycheck;

/**
 * Created by Silvan on 14.10.2017.
 */

import ch.meienberger.android.laundrycheck.adapter.WashorderRecyclerViewContentAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ch.meienberger.android.common.logger.Log;


import ch.meienberger.android.laundrycheck.R;

import java.util.ArrayList;
import java.util.List;

import ch.meienberger.android.SQL.LaundrycheckDataSource;
import ch.meienberger.android.laundrycheck.adapter.WashorderAdapter;

public class WashorderDetailViewFragment extends Fragment {

    public static final String ARG_WASHORDERID = "arg_washorderid";
    private static final String TAG = "WashorderDetailViewFragment";

    private static long mWashorderId;
    private LaundrycheckDataSource dataSource;


    protected EditText mEditTextName;
    protected EditText mEditTextAddress;
    //TODO
    protected Washorder mWashorder;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //get selected WashorderId
        Bundle args = getArguments();
        mWashorderId = args.getLong(ARG_WASHORDERID,0);

        Log.d(TAG, "Washorder with Id: " + mWashorderId + " selected.");
        dataSource = new LaundrycheckDataSource(this.getContext());
        dataSource.open();
        mWashorder = dataSource.getWashorder(mWashorderId);
        dataSource.close();



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.washorder_detail_view, container, false);
        rootView.setTag(TAG);

        Log.d(TAG, "Washorder with Id: " + mWashorder.getId() + " is displaying.");



        // BEGIN_INCLUDE
        mEditTextName = (EditText) rootView.findViewById(R.id.washorderdetail_name_editText);
        mEditTextAddress = (EditText) rootView.findViewById(R.id.washorderdetail_address_editText);
        //TODO

        // END_INCLUDE(

        //Fill Fields
        mEditTextName.setText(mWashorder.getName());
        mEditTextAddress.setText(mWashorder.getAddress());

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

        Log.d(TAG, "method onResume is called. DB is getting opened");
        dataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "method onPause is called. Date is going to be saved and DB is getting closed");


        dataSource.close();
    }








}
