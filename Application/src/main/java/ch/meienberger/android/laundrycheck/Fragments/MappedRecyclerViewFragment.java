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

import java.util.ArrayList;

import ch.meienberger.android.SQL.MappingDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.adapter.MappingAdapter;
import ch.meienberger.android.laundrycheck.adapter.MappingTouchCallback;
import ch.meienberger.android.laundrycheck.custom_class_objects.Mapping;
import ch.meienberger.android.laundrycheck.custom_class_objects.Washorder;


public class MappedRecyclerViewFragment extends Fragment {

    private static final String TAG = MappedRecyclerViewFragment.class.getSimpleName();
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    public static final String ARG_WASHORDERID = "arg_washorderid";

    private MappingDataSource dataSource;
    private FloatingActionButton mButtonAddClothes;
    private Washorder mCurWashorder;

    public MappedRecyclerViewFragment() {
    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected Button mClickedItem;

    protected RecyclerView mRecyclerView;
    protected MappingAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Mapping> mDataset;
    protected long mWashorderId_long;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mapped_detail_scrollcontainer, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.mapped_recyclerView);
        mButtonAddClothes = (FloatingActionButton) rootView.findViewById(R.id.button_mapping_clothes);


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
        dataSource = new MappingDataSource(this.getContext());

        //get washorder from bundle
        Bundle args = getArguments();

        mWashorderId_long = (args.getLong(ARG_WASHORDERID));
        int mWashorderId = Integer.parseInt(String.valueOf(mWashorderId_long));

        dataSource.open();
        mDataset = (ArrayList<Mapping>) dataSource.getMappingfromWashorder(mWashorderId);
        dataSource.close();

        mAdapter = new MappingAdapter(mDataset, dataSource,getContext());
        // Set custom Washorderadapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new MappingTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        // END_INCLUDE(initializeRecyclerView)


        mButtonAddClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment with the WashorderId as arg and a new transaction
                Fragment selectmappingfragment = new SelectMappingClothesRecyclerViewFragment();

                //bundle the washorder id
                Bundle args = new Bundle();
                args.putLong(SelectMappingClothesRecyclerViewFragment.ARG_WASHORDERID,mWashorderId_long);
                selectmappingfragment.setArguments(args);


                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.content_fragment, selectmappingfragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
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
        curActivity.setTitle(R.string.mapping);

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
