/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ch.meienberger.android.laundrycheck;

import ch.meienberger.android.SQL.WashorderDataSource;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.meienberger.android.common.logger.Log;


import java.util.ArrayList;

import ch.meienberger.android.laundrycheck.adapter.WashorderAdapter;



public class WashorderRecyclerViewFragment extends Fragment {

    private static final String TAG = "WashorderRecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private WashorderDataSource dataSource;

    public WashorderRecyclerViewFragment() {
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected FloatingActionButton mButtonAddItem;
    protected Button mClickedItem;
    protected Button mButtonRemoveItem;

    protected RecyclerView mRecyclerView;
    protected WashorderAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Washorder> mDataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.washorder_recycler_view_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.washorders_recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //get already stored data to the adapter
        //init SQL
        dataSource = new WashorderDataSource(this.getContext());
        dataSource.open();
        mDataset = (ArrayList<Washorder>)dataSource.getAllWashorders();
        dataSource.close();

        mAdapter = new WashorderAdapter(mDataset, dataSource, this);
        // Set custom Washorderadapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new CustomTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        // END_INCLUDE(initializeRecyclerView)

        mButtonAddItem = (FloatingActionButton) rootView.findViewById(R.id.button_add_washorder);
        mButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addItem();
            }
        });


        return rootView;
    }

    public void callWashorderDetailFragment(){

    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Calls Detail Fragment with the right Id
     */
    public void callWashorderDetailFragment(long cur_WashorderId){
        return;
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

    Log.d(TAG, "method onPause is called. DB is getting closed");
    dataSource.close();
    }







}
