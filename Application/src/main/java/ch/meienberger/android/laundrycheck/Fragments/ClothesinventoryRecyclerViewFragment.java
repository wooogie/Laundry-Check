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

    public final class TaskResult {
        private Bitmap mbitmap;
        private int mposition;

        TaskResult(Bitmap bitmap, int position){
            mbitmap = bitmap;
            mposition = position;
        }

        public Bitmap getBitmap() {
            return mbitmap;
        }

        public int getPosition() {
            return mposition;
        }
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

                    for(int i = 0;i<mRecyclerView.getChildCount();++i) {

                        curChild = mRecyclerView.getChildAt(i);

                        //Create for all preview pictures a own Task for getting the right resolution.
                        //Set Picturepreview by a new thread
                        LoadPreviewPictureTask loadpreviewpicture = new LoadPreviewPictureTask();
                        loadpreviewpicture.execute(
                                String.valueOf(mRecyclerView.getChildAdapterPosition(curChild)),
                                String.valueOf(getActivity().getResources().getDimensionPixelSize(R.dimen.image_preview_with)),
                                String.valueOf(getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_height)),
                                mDataset.get(mRecyclerView.getChildAdapterPosition(curChild)).getPicture(),
                                String.valueOf(mRecyclerView.getChildLayoutPosition(curChild)));
                    }
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
     * This task loads the picture in the right resolution into the Viewholder.
     * It is going to run in the background because it need much time to do the downscaling and intrrupt else the UI.
     * params([0]=position,[1]=imageWith,[2]=imageHeight,[3]=Picturepath,[4]=Layoutposition,
     * */
    public class LoadPreviewPictureTask extends AsyncTask<String, Integer, TaskResult> {

        private final String LOG_TAG = LoadPreviewPictureTask.class.getSimpleName();

        protected TaskResult doInBackground(String... params) {


            // Get Parameter of task
            int position = Integer.parseInt(params[0]);
            int targetW = Integer.parseInt(params[1]);
            int targetH = Integer.parseInt(params[2]);
            String picturepath = String.valueOf(params[3]);
            int layoutposition = Integer.parseInt(params[4]);


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturepath.replace("file:", ""), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (photoH > targetH || photoW > targetW)
        {
            scaleFactor = photoW > photoH
                    ? photoH / targetH
                    : photoW / targetW;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(picturepath.replace("file:",""), bmOptions);


            return new TaskResult(bitmap,layoutposition);
        }


        protected void onPostExecute(TaskResult result) {
            Log.d(TAG, "hintergroundtask fertig mit" + result.getPosition());
            final View holder = (View)mRecyclerView.getChildAt(result.getPosition());
            //set bitmap
            mAdapter.updatePreviewpicture(result.getPosition(),result.mbitmap);
        }

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
