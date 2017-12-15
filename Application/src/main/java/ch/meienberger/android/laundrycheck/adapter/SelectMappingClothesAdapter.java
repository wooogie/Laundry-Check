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

package ch.meienberger.android.laundrycheck.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.meienberger.android.SQL.MappingDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class SelectMappingClothesAdapter extends RecyclerView.Adapter<SelectMappingClothesAdapter.ViewHolder>  {
    private static final String TAG = SelectMappingClothesAdapter.class.getSimpleName();
    private static MappingDataSource mdataSource;
    private static ArrayList<Clothes> mDataSet;
    private static int washorderId;
    private static View mView;



    // BEGIN_INCLUDE
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final ImageView preview_image;
        private final RecyclerView recyclerview;


        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clothesId = Integer.parseInt(getId().getText().toString());
                    Clothes curClothes=null;
                    for (int i = 0;i<mDataSet.size();i++) {
                        if (mDataSet.get(i).getId()==clothesId){
                            curClothes = mDataSet.get(i);
                        }

                    }

                    Log.d(TAG, "Clothes with Id: " + clothesId + " clicked and gets mapped");


                    if(mdataSource.ClothesIsAlreadyMapped(washorderId,clothesId)) {
                        //notify user that clothes is already mapped to this washorder
                        Snackbar.make(mView, R.string.clothes_already_mapped, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        mdataSource.createMapping(washorderId,clothesId);
                       // mDataSet.remove(curClothes);

                        //notify user that clothes is mapped to this washorder
                        Snackbar.make(mView, R.string.clothes_mapped, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
            });

            //link view holder to xml layout
            name = (TextView) v.findViewById(R.id.clothesrow_name_textview);
            id = (TextView) v.findViewById(R.id.clothesrow_id_textview);
            preview_image = (ImageView) v.findViewById(R.id.clothesrowitem_preview_imageView);
            recyclerview = (RecyclerView) v.findViewById(R.id.mapped_recyclerView);

        }

        public TextView getName() {
            return name;
        }
        public TextView getId() {
            return id;
        }
        public ImageView getPreview_image() {return preview_image; }
        public RecyclerView getRecyclerview() {return recyclerview; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public SelectMappingClothesAdapter(ArrayList<Clothes> dataSet, MappingDataSource dataSource,int curWashorderId,View curView) {
        mDataSet = dataSet;
        mdataSource = dataSource;
        washorderId = curWashorderId;
        mView = curView;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.clothes_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Clothes " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getName().setText(mDataSet.get(position).getName());

        String tmp_string = Long.toString(mDataSet.get(position).getId());
        viewHolder.getId().setText(tmp_string);

        setPreviewPicture(viewHolder,position);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addItem() {

    }

    public void removeItem() {
        mDataSet.remove(mDataSet.size()-1);
        notifyItemRemoved(mDataSet.size());
    }


    public void removeItemAt(int position) {


    }

    private void setPreviewPicture(ViewHolder viewHolder,final int position) {
        // Get the dimensions of the View
        int targetW = mView.getResources().getDimensionPixelSize(R.dimen.image_preview_with);
        int targetH = mView.getResources().getDimensionPixelSize(R.dimen.list_item_height);
        String PhotoPath = mDataSet.get(position).getPicture();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(PhotoPath.replace("file:", ""), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(PhotoPath.replace("file:",""), bmOptions);
        viewHolder.getPreview_image().setImageBitmap(bitmap);
    }

    private int getDatasetPosition(Clothes searchedClothes){
        for (int i=0;i<mDataSet.size();i++){
            if (mDataSet.get(i)==searchedClothes) {
                return i;
            }
                    }
                return 0;
    }

}
