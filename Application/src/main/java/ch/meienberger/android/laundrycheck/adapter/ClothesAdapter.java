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

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import ch.meienberger.android.SQL.ClothesDataSource;

import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.Fragments.ClothesinventoryRecyclerViewFragment;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.Fragments.ClothesDetailViewFragment;
import ch.meienberger.android.laundrycheck.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder>  {
    private static final String TAG = ClothesAdapter.class.getSimpleName();
    private static ClothesDataSource mdataSource;
    private ArrayList<Clothes> mDataSet;
    private static Fragment mparentFragment;
    private static ViewGroup mviewgroup;
    private static Map<String,Bitmap> mbitmaps = new Hashtable<String,Bitmap>();

    // BEGIN_INCLUDE
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;
        private final ImageView preview_image;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clothes with Id: " + getId().getText() + " clicked.");

                    // Create new fragment with the ClothesId as arg and a new transaction
                    Fragment DetailFragment = new ClothesDetailViewFragment();
                    Bundle args = new Bundle();
                    args.putLong(ClothesDetailViewFragment.ARG_CLOTHESID, Long.parseLong(getId().getText().toString()));
                    DetailFragment.setArguments(args);

                    android.support.v4.app.FragmentTransaction transaction = mparentFragment.getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack if needed
                    transaction.replace(R.id.content_fragment, DetailFragment);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();

                }
            });



            //link view holder to xml layout
            name = (TextView) v.findViewById(R.id.clothesrow_name_textview);
            id = (TextView) v.findViewById(R.id.clothesrow_id_textview);
            preview_image = (ImageView) v.findViewById(R.id.clothesrowitem_preview_imageView);
        }

        public TextView getName() {
            return name;
        }
        public TextView getId() {
            return id;
        }
        public ImageView getPreview_image() {return preview_image; }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ClothesAdapter(ArrayList<Clothes> dataSet, ClothesDataSource dataSource, Fragment parentFragment) {
        mDataSet = dataSet;
        mdataSource = dataSource;
        mparentFragment = parentFragment;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.clothes_row_item, viewGroup, false);

        mviewgroup = viewGroup;

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Clothes " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getName().setText(mDataSet.get(position).getName());

        String tmp_string = Long.toString(mDataSet.get(position).getId());
        viewHolder.getId().setText(tmp_string);

        Bitmap curBitmap = mbitmaps.get(mDataSet.get(position).getPicture());
        if(curBitmap != null){
            viewHolder.getPreview_image().setImageBitmap(curBitmap);
        }

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addItem() {

        //generate Name
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String formattedDate = df.format(c.getTime());


        //generate new clothes
        mdataSource.open();
        mdataSource.createClothes(formattedDate);
        mDataSet = (ArrayList<Clothes>)mdataSource.getAllClothes();
        mdataSource.close();

        notifyItemInserted(0);
        notifyItemChanged(0);
    }


    public void removeItemAt(int position) {
        final Clothes toDeleteClothes = mDataSet.get(position);
        final int cur_Position = position;



        //double check if the click was not a misstake
        AlertDialog.Builder builder = new AlertDialog.Builder(mparentFragment.getContext());
        builder.setMessage(R.string.confirm_deleteClothes_message)
                .setTitle(R.string.confirm_delete_title);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //delete picture
                File oldPhotoFile = new File(toDeleteClothes.getPicture());
                oldPhotoFile.delete();

                //delete from DB
                mdataSource.open();
                mdataSource.deleteClothes(toDeleteClothes);
                mdataSource.close();

                //remove from Dataset
                mDataSet.remove(cur_Position);
                notifyItemRemoved(cur_Position);

                           }
        });

        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyItemChanged(cur_Position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updatePreviewpicture(int position, Bitmap bitmap) {
        String curPicturepath = mDataSet.get(position).getPicture();
        if (!curPicturepath.isEmpty()){
            mbitmaps.put(curPicturepath,bitmap);
            notifyItemChanged(position);
        }
    }


    }
