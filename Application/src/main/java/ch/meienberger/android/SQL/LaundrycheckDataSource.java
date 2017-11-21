package ch.meienberger.android.SQL;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

import ch.meienberger.android.laundrycheck.Washorder;
import ch.meienberger.android.laundrycheck.adapter.WashorderAdapter;


public class LaundrycheckDataSource {

    private static final String LOG_TAG = LaundrycheckDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private LaundrycheckDbHelper dbHelper;
    private String[] washorder_columns = {
            LaundrycheckDbHelper.COLUMN_ID,
            LaundrycheckDbHelper.COLUMN_NAME,
            LaundrycheckDbHelper.COLUMN_ADDRESS,
            LaundrycheckDbHelper.COLUMN_CLOTHES_COUNT,
            LaundrycheckDbHelper.COLUMN_DELIVERY_DATE,
            LaundrycheckDbHelper.COLUMN_PICKUP_DATE,
            LaundrycheckDbHelper.COLUMN_PRICE,
            LaundrycheckDbHelper.COLUMN_COMMENTS
    };

/*
    Db Helper gets created
 */
    public LaundrycheckDataSource(Context context) {
        Log.d(LOG_TAG, "Datasource creates the dbHelper.");
        dbHelper = new LaundrycheckDbHelper(context);
    }

    /*
    Db gets open as writable
     */
    public void open() {
        Log.d(LOG_TAG, "Reference on Db gets requested.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Reference is available. Path of Db: " + database.getPath());
    }

    /*
    Db gets closed
     */
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Db closed by DbHelper.");
    }


    /*
    Insert a new Washorder into the DB and return the insered as Washorder
     */
    public Washorder createWashorder(String name) {
        ContentValues values = new ContentValues();
        values.put(LaundrycheckDbHelper.COLUMN_NAME, name);
        values.put(LaundrycheckDbHelper.COLUMN_ADDRESS, "");
        values.put(LaundrycheckDbHelper.COLUMN_DELIVERY_DATE, "");
        values.put(LaundrycheckDbHelper.COLUMN_PICKUP_DATE, "");
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_COUNT, 0);
        values.put(LaundrycheckDbHelper.COLUMN_PRICE, 0);
        values.put(LaundrycheckDbHelper.COLUMN_COMMENTS, "");


        long insertId = database.insert(LaundrycheckDbHelper.TABLE_WASH_ORDERS, null, values);

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
                washorder_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Washorder washorder = cursorToWashorder(cursor);
        cursor.close();

        return washorder;
    }


    private Washorder cursorToWashorder(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_NAME);
        int idAddress = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ADDRESS);
        int idDeliveryDate = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_DELIVERY_DATE);
        int idPickupDate = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PICKUP_DATE);
        int idClothesCount = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_CLOTHES_COUNT);
        int idPrice = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PRICE);
        int idComments = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_COMMENTS);

        String name = cursor.getString(idName);
        String address = cursor.getString(idAddress);
        String deliverydate = cursor.getString(idDeliveryDate);
        String pickupdate = cursor.getString(idPickupDate);
        String comments = cursor.getString(idComments);
        int clothescount = cursor.getInt(idClothesCount);
        int price = cursor.getInt(idPrice);
        long id = cursor.getLong(idIndex);

        Washorder washorder = new Washorder();
        washorder.setId(id);
        washorder.setAddress(address);
        washorder.setDelivery_date(deliverydate);
        washorder.setPickup_date(pickupdate);
        washorder.setClothes_count(clothescount);
        washorder.setComments(comments);
        washorder.setName(name);
        washorder.setPrice(price);

        return washorder;
    }


    public List<Washorder> getAllWashorders() {
        List<Washorder> washorderslist = new ArrayList<>();

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
                washorder_columns, null, null, null, null, LaundrycheckDbHelper.COLUMN_ID +" DESC");

        cursor.moveToFirst();
        Washorder washorder;

        while(!cursor.isAfterLast()) {
            washorder = cursorToWashorder(cursor);
            washorderslist.add(washorder);
            Log.d(LOG_TAG, "ID: " + washorder.getId() + ", Inhalt: " + washorder.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return washorderslist;
    }

    public void deleteWashorder(Washorder curWashorder){
        long id = curWashorder.getId();

        database.delete(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
                LaundrycheckDbHelper.COLUMN_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Washorder deletet! ID: " + id);

    }
}
