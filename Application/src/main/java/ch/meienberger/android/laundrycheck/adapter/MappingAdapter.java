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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import ch.meienberger.android.SQL.MappingDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.custom_class_objects.Mapping;

/**
 * Provide views to RecyclerView with data from mMappingDataSet.
 */
public class MappingAdapter extends RecyclerView.Adapter<MappingAdapter.ViewHolder>  {
    private static final String TAG = MappingAdapter.class.getSimpleName();
    private static MappingDataSource mMappingdataSource;
    private static Context mContext;
    private static ArrayList<Mapping> mMappingDataSet;

    // BEGIN_INCLUDE
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final ImageView preview_image;
        private final RelativeLayout background;
        private Mapping MappingHolder;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curMappingId = Integer.valueOf(getId().getText().toString());
                    Log.d(TAG, "Mapping with Id: " + curMappingId + " clicked.");

                    mMappingdataSource.open();
                    final Clothes curClothes = mMappingdataSource.getClothes(MappingHolder.getClothes_id());

                    int backgroundcolor;
                    if(MappingHolder.isClothes_returned()){

                        //double check if the click was not a misstake
                        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)mContext));
                        builder.setMessage(R.string.confirm_undo_message)
                        .setTitle(R.string.confirm_undo_title);
                        builder.setCancelable(false);
                        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                curClothes.undo_washed();
                                int backgroundcolor;
                                backgroundcolor = mContext.getColor(R.color.colorNotReturned);
                                MappingHolder.setClothes_returned(false);
                                getBackground().setBackgroundColor(backgroundcolor);
                                curClothes.undo_washed();
                            }
                        });

                        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{
                        backgroundcolor = mContext.getColor(R.color.colorReturned);
                        MappingHolder.setClothes_returned(true);
                        getBackground().setBackgroundColor(backgroundcolor);
                        curClothes.washed();
                    }


                    //Save changes to DB and Dataset
                    mMappingdataSource.updateMapping(MappingHolder);
                    mMappingdataSource.updateClothes(curClothes);
                    mMappingdataSource.close();

                }
            });

            //link view holder to xml layout
            name = (TextView) v.findViewById(R.id.mappedrow_name_textview);
            id = (TextView) v.findViewById(R.id.mappedrow_id_textview);
            preview_image = (ImageView) v.findViewById(R.id.mappedrow_item_preview_imageView);
            background = (RelativeLayout) v.findViewById(R.id.mappedrow_background_relativeLayout);

        }

        public TextView getName() {
            return name;
        }
        public TextView getId() {
            return id;
        }
        public ImageView getPreview_image() {return preview_image; }
        public RelativeLayout getBackground() {return background; }
        public void setMappingHolder(Mapping mapping) {MappingHolder = mapping; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public MappingAdapter(ArrayList<Mapping> dataSet, MappingDataSource dataSource, Context curContext) {
        mMappingDataSet = dataSet;
        mMappingdataSource = dataSource;
        mContext  = curContext;

    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mapped_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Mapping " + position + " set.");
        Mapping mMapping = mMappingDataSet.get(position);
        Clothes mMappedClothes = mMappingdataSource.getClothes(mMapping.getClothes_id());
        viewHolder.setMappingHolder(mMapping);


        // Get element from dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getName().setText(mMappedClothes.getName());

        String tmp_string = Long.toString(mMappingDataSet.get(position).getId());
        viewHolder.getId().setText(tmp_string);

        int backgroundcolor;
        if(mMapping.isClothes_returned()){
            backgroundcolor = mContext.getColor(R.color.colorReturned);
        }else{
            backgroundcolor = mContext.getColor(R.color.colorNotReturned);
        }
        viewHolder.getBackground().setBackgroundColor(backgroundcolor);

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMappingDataSet.size();
    }

    // Return Dataset Mapping by id
    public Mapping getDataSetMapping(long id){
        int i = 0;
        for(i=0;i>mMappingDataSet.size();i++){
            if(mMappingDataSet.get(i).getId() == id){
                return mMappingDataSet.get(i);
            }
            }
            return null;
        }

    public void addItem() {

    }

    public void removeItem() {
        mMappingDataSet.remove(mMappingDataSet.size()-1);
        notifyItemRemoved(mMappingDataSet.size());
    }

    public void removeItemAt(int position) {



    }

    private void setPreviewPicture(ViewHolder viewHolder,final int position) {
        // Get the dimensions of the View
        int targetW = viewHolder.getPreview_image().getWidth();
        int targetH = viewHolder.getPreview_image().getHeight();
        String PhotoPath = mMappingdataSource.getClothes(mMappingDataSet.get(position).getClothes_id()).getPicture();

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

}
