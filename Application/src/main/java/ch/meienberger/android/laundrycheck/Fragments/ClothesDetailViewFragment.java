package ch.meienberger.android.laundrycheck.Fragments;

/**
 * Created by Silvan on 14.10.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ch.meienberger.android.SQL.ClothesDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.R;

import static ch.meienberger.android.laundrycheck.Fragments.SettingsViewFragment.PREF_USE_RFID;

public class ClothesDetailViewFragment extends Fragment {

    public static final String ARG_CLOTHESID = "arg_clothesid";
    private static final String TAG = "ClothesDetailViewFragment";

    private long mClothesid;
    private ClothesDataSource dataSource;


    protected EditText mEditTextName;
    protected EditText mEditTextRfidId;
    protected TextView mTextViewRfidIdLable;
    protected android.widget.Space mSpaceRfidId;
    protected EditText mEditTextWashcount;
    protected EditText mEditTextLastwashed;
    protected EditText mEditTextPieces;
    protected EditText mEditTextClothestype;

    protected static Clothes mClothes = new Clothes();


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        mTextViewRfidIdLable = (TextView) rootView.findViewById(R.id.rfid_id_lable);
        mEditTextRfidId = (EditText) rootView.findViewById(R.id.clothesdetail_rfid_id_editText);
        mSpaceRfidId = (android.widget.Space) rootView.findViewById(R.id.clothesdetail_rfidid_lable);
        mEditTextWashcount = (EditText) rootView.findViewById(R.id.clothesdetail_washcount_editText);
        mEditTextLastwashed = (EditText) rootView.findViewById(R.id.clothesdetail_lastwashed_editText);
        mEditTextPieces = (EditText) rootView.findViewById(R.id.clothesdetail_pieces_editText);
        mEditTextClothestype = (EditText) rootView.findViewById(R.id.clothesdetail_clothestype_editText);
        // END_INCLUDE(

        //Fill Fields
        mEditTextName.setText(mClothes.getName());
        mEditTextRfidId.setText(mClothes.getRfid_id());
        mEditTextLastwashed.setText(mClothes.getLast_washed());
        mEditTextWashcount.setText(String.valueOf(mClothes.getWashcount()));
        mEditTextPieces.setText(String.valueOf(mClothes.getPieces()));
        mEditTextClothestype.setText(String.valueOf(mClothes.getClothestype()));

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

        //Hide RFID if necessary
        //get shared preferences from main activity
        final SharedPreferences sharedpreferences = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(PREF_USE_RFID,true)){
            Log.d(TAG, "RFID is not used, hide fields");
            mEditTextRfidId.setVisibility(View.GONE);
            mTextViewRfidIdLable.setVisibility(View.GONE);
            mSpaceRfidId.setVisibility(View.GONE);
        }

        Log.d(TAG, "method onResume is called. DB is getting opened");
        dataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "method onPause is called. Date is going to be saved and DB is getting closed");
        mClothes.setName(mEditTextName.getText().toString());
        mClothes.setRfid_id(mEditTextRfidId.getText().toString());
        mClothes.setPieces(Integer.parseInt(mEditTextPieces.getText().toString()));
        mClothes.setClothestype(Integer.parseInt(mEditTextClothestype.getText().toString()));

        dataSource.updateClothes(mClothes);
        dataSource.close();

        //notify user
        Snackbar.make(getView(), R.string.changes_saved, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }







}
