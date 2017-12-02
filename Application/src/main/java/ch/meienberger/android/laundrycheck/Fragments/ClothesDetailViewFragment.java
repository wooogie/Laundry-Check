package ch.meienberger.android.laundrycheck.Fragments;

/**
 * Created by Silvan on 14.10.2017.
 */

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.meienberger.android.SQL.ClothesDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.R;

public class ClothesDetailViewFragment extends Fragment {

    public static final String ARG_CLOTHESID = "arg_clothesid";
    private static final String TAG = "ClothesDetailViewFragment";

    private long mClothesid;
    private ClothesDataSource dataSource;


    protected EditText mEditTextName;
    //TODO
    protected static Clothes mClothes = new Clothes();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //get selected WashorderId
        Bundle args = getArguments();

        mClothesid = args.getLong(ARG_CLOTHESID,0);
        Log.d(TAG, "Clothes with Id: " + mClothesid + " selected.");

        dataSource = new ClothesDataSource(this.getContext());
        dataSource.open();
        mClothes = dataSource.getClothes(mClothesid);
        dataSource.close();



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.clothes_detail_view, container, false);
        rootView.setTag(TAG);

        Log.d(TAG, "Washorder with Id: " + mClothes.getId() + " is displaying.");



        // BEGIN_INCLUDE
        mEditTextName = (EditText) rootView.findViewById(R.id.clothesdetail_name_editText);


        // END_INCLUDE(

        //Fill Fields
        mEditTextName.setText(mClothes.getName());
//// TODO: 30.11.2017
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
        mClothes.setName(mEditTextName.getText().toString());

        dataSource.updateClothes(mClothes);

        Snackbar.make(null, R.string.changes_saved, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        dataSource.close();
    }







}
