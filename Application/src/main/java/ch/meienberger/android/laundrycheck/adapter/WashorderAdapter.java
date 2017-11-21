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

import ch.meienberger.android.SQL.LaundrycheckDataSource;
import ch.meienberger.android.SQL.LaundrycheckDbHelper;
import ch.meienberger.android.common.logger.Log;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import ch.meienberger.android.laundrycheck.R;

import java.util.ArrayList;
import java.util.List;

import ch.meienberger.android.laundrycheck.MainActivity;
import ch.meienberger.android.laundrycheck.WashorderNameComparator;
import ch.meienberger.android.laundrycheck.WashorderRecyclerViewFragment;
import ch.meienberger.android.laundrycheck.Washorder;

import static ch.meienberger.android.common.ISO8601.now;
import static ch.meienberger.android.common.ISO8601.toCalendar;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class WashorderAdapter extends RecyclerView.Adapter<WashorderAdapter.ViewHolder>  {
    private static final String TAG = "WashorderAdapter";
    private static LaundrycheckDataSource mdataSource;
    private ArrayList<Washorder> mDataSet;

    public void setDataSource(LaundrycheckDataSource dataSource) {
        this.mdataSource = dataSource;
    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public WashorderAdapter(ArrayList<Washorder> dataSet, LaundrycheckDataSource dataSource) {
        //dataSource = new LaundrycheckDataSource(this.);
        mDataSet = dataSet;
        mdataSource = dataSource;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.washorder_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        //viewHolder.getTextView().setText(mDataSet.get(position));
        viewHolder.getTextView().setText(mDataSet.get(position).getName());
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


        //generate new washorder
        mdataSource.open();
        mdataSource.createWashorder(formattedDate);

        mDataSet = (ArrayList<Washorder>)mdataSource.getAllWashorders();

        mdataSource.close();

        //Collections.sort(mDataSet, new WashorderNameComparator());

        //(notifyItemInserted(mDataSet.size()-1);
        notifyItemInserted(0);
    }

    public void removeItem() {
        mDataSet.remove(mDataSet.size()-1);
        notifyItemRemoved(mDataSet.size());
    }

    public void removeItemAt(int position) {

        //delete from DB
        mdataSource.open();
        mdataSource.deleteWashorder(mDataSet.get(position));
        mdataSource.close();

        //remove from Dataset
        mDataSet.remove(position);
        notifyItemRemoved(position);


    }




}
