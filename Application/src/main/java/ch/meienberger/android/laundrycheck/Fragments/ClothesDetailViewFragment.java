package ch.meienberger.android.laundrycheck.Fragments;

/**
 * Created by Silvan on 14.10.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.meienberger.android.SQL.ClothesDataSource;
import ch.meienberger.android.common.logger.Log;
import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.R;

import static android.app.Activity.RESULT_OK;
import static ch.meienberger.android.laundrycheck.Fragments.SettingsViewFragment.PREF_USE_RFID;

public class ClothesDetailViewFragment extends Fragment {

    public static final String ARG_CLOTHESID = "arg_clothesid";
    private static final String TAG = "ClothesDetailViewFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    private long mClothesid;
    private ClothesDataSource dataSource;

    protected ImageView mImageViewPreview;
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
        View rootView = inflater.inflate(R.layout.clothes_detail_scrollcontainer, container, false);
        rootView.setTag(TAG);

        Log.d(TAG, "Washorder with Id: " + mClothes.getId() + " is displaying.");



        // BEGIN_INCLUDE
        mImageViewPreview = (ImageView) rootView.findViewById(R.id.clothesdetail_preview_imageView);
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
        mEditTextClothestype.setText(mClothes.getClothestype().name());


        //Listener
        mImageViewPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });



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

        //set picture
        if (!mClothes.getPicture().equalsIgnoreCase("")){
            Bitmap bitmap = BitmapFactory.decodeFile(mClothes.getPicture().replace("file:",""));
            mImageViewPreview.setImageBitmap(bitmap);
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
        mClothes.setClothestype(Clothes.Clothestype.valueOf(mEditTextClothestype.getText().toString()));

        dataSource.updateClothes(mClothes);
        dataSource.close();

        //notify user
        Snackbar.make(getView(), R.string.changes_saved, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(((Activity) getContext()).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //// TODO: 02.12.2017
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile((Activity) getContext(),
                        "ch.meienberger.android.laundrycheck",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ((Activity) getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mClothes.setPicture("file:" + image.getAbsolutePath());
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageViewPreview.getWidth();
        int targetH = mImageViewPreview.getHeight();
        String PhotoPath = mClothes.getPicture();

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
        mImageViewPreview.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageViewPreview.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }







}
