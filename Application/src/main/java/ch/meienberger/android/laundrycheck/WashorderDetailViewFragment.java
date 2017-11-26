package ch.meienberger.android.laundrycheck;

/**
 * Created by Silvan on 14.10.2017.
 */

import ch.meienberger.android.SQL.WashorderDataSource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import ch.meienberger.android.common.logger.Log;

public class WashorderDetailViewFragment extends Fragment {

    public static final String ARG_WASHORDERID = "arg_washorderid";
    private static final String TAG = "WashorderDetailViewFragment";

    private static long mWashorderId;
    private WashorderDataSource dataSource;


    protected EditText mEditTextName;
    protected EditText mEditTextAddress;
    protected EditText mEditTextDeliveryDate;
    protected EditText mEditTextPickupDate;
    protected EditText mEditTextPrice;
    protected EditText mEditTextComment;
    protected Washorder mWashorder;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //get selected WashorderId
        Bundle args = getArguments();
        mWashorderId = args.getLong(ARG_WASHORDERID,0);

        Log.d(TAG, "Washorder with Id: " + mWashorderId + " selected.");
        dataSource = new WashorderDataSource(this.getContext());
        dataSource.open();
        mWashorder = dataSource.getWashorder(mWashorderId);
        dataSource.close();



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.washorder_detail_view, container, false);
        rootView.setTag(TAG);

        Log.d(TAG, "Washorder with Id: " + mWashorder.getId() + " is displaying.");



        // BEGIN_INCLUDE
        mEditTextName = (EditText) rootView.findViewById(R.id.washorderdetail_name_editText);
        mEditTextAddress = (EditText) rootView.findViewById(R.id.washorderdetail_address_editText);
        mEditTextDeliveryDate = (EditText) rootView.findViewById(R.id.washorderdetail_delivery_editText);
        mEditTextPickupDate = (EditText) rootView.findViewById(R.id.washorderdetail_pickup_editText);
        mEditTextPrice = (EditText) rootView.findViewById(R.id.washorderdetail_price_editText);
        mEditTextComment = (EditText) rootView.findViewById(R.id.washorderdetail_comment_editText);
        mEditTextAddress = (EditText) rootView.findViewById(R.id.washorderdetail_address_editText);

        // END_INCLUDE(

        //Fill Fields
        mEditTextName.setText(mWashorder.getName());
        mEditTextAddress.setText(mWashorder.getAddress());
        mEditTextDeliveryDate.setText(mWashorder.getDelivery_date());
        mEditTextPickupDate.setText(mWashorder.getPickup_date());
        mEditTextPrice.setText(String.valueOf(mWashorder.getPrice()));
        mEditTextComment.setText(mWashorder.getComments());

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
        mWashorder.setName(mEditTextName.getText().toString());
        mWashorder.setAddress(mEditTextAddress.getText().toString());
        mWashorder.setDelivery_date(mEditTextDeliveryDate.getText().toString());
        mWashorder.setPickup_date(mEditTextPickupDate.getText().toString());
        mWashorder.setPrice(Integer.parseInt(mEditTextPrice.getText().toString()));
        mWashorder.setComments(mEditTextComment.getText().toString());
        dataSource.updateWashorder(mWashorder);
        dataSource.close();
    }








}
