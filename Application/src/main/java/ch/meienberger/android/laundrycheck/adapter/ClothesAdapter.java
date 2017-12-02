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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ch.meienberger.android.SQL.ClothesDataSource;

import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.Fragments.ClothesDetailViewFragment;
import ch.meienberger.android.laundrycheck.Fragments.ClothesinventoryRecyclerViewFragment;
import ch.meienberger.android.laundrycheck.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder>  {
    private static final String TAG = "ClothesAdapter";
    private static ClothesDataSource mdataSource;
    private ArrayList<Clothes> mDataSet;
    private static Fragment mparentFragment;

    // BEGIN_INCLUDE
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView id;

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
            name = (TextView) v.findViewById(R.id.clothes_row_name);
            id = (TextView) v.findViewById(R.id.clothes_row_id);
        }

        public TextView getName() {
            return name;
        }
        public TextView getId() {
            return id;
        }
    }
    // END_INCLUDE

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

    public void removeItem() {
        mDataSet.remove(mDataSet.size()-1);
        notifyItemRemoved(mDataSet.size());
    }

    public void removeItemAt(int position) {

        //// TODO: 30.11.2017 doublecheck
        //delete from DB
        mdataSource.open();
        mdataSource.deleteClothes(mDataSet.get(position));
        mdataSource.close();

        //remove from Dataset
        mDataSet.remove(position);
        notifyItemRemoved(position);

    }

}