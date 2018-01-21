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

package ch.meienberger.android.laundrycheck.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecyclerListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;

import ch.meienberger.android.SQL.ClothesDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.adapter.ClothesTouchCallback;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.adapter.ClothesAdapter;


public class ClothesinventoryRecyclerViewFragment extends Fragment {

    private static final String TAG = ClothesinventoryRecyclerViewFragment.class.getSimpleName();
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private ClothesDataSource dataSource;


    public ClothesinventoryRecyclerViewFragment() {
    }

        private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected FloatingActionButton mButtonAddItem;

    protected RecyclerView mRecyclerView;
    protected ClothesAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Clothes> mDataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.clothesinventory_recycler_view_frag, container, false);
        rootView.setTag(TAG);
        final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(){

            @Override
            public void onGlobalLayout(){
                View curChild;

                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.clothes_recyclerView);

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
        dataSource = new ClothesDataSource(this.getContext());
        dataSource.open();
        mDataset = (ArrayList<Clothes>)dataSource.getAllClothes();
        dataSource.close();

        mAdapter = new ClothesAdapter(mDataset, dataSource, this);
        // Set custom Clothesadapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);



        ItemTouchHelper.Callback callback = new ClothesTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
        // END_INCLUDE(initializeRecyclerView)

        mButtonAddItem = (FloatingActionButton) rootView.findViewById(R.id.button_add_clothes);
        mButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addItem();
                mLayoutManager.scrollToPosition(0);
            }
        });

        return rootView;
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
     * Lifecycle methods
     */
    @Override
    public void onResume() {
        super.onResume();
        Activity curActivity =  ((Activity) getContext());
        curActivity.setTitle(R.string.action_clothes);
    }

    @Override
    public void onPause() {
    super.onPause();

    Log.d(TAG, "method onPause is called. DB is getting closed");
    dataSource.close();
    }

}
