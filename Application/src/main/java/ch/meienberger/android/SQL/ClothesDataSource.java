package ch.meienberger.android.SQL;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.meienberger.android.laundrycheck.Clothes;
import ch.meienberger.android.laundrycheck.Washorder;


public class ClothesDataSource {

    private static final String LOG_TAG = ClothesDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private LaundrycheckDbHelper dbHelper;
    private String[] clothes_columns = {
            LaundrycheckDbHelper.COLUMN_ID,
            LaundrycheckDbHelper.COLUMN_NAME,
            LaundrycheckDbHelper.COLUMN_RFID_ID,
            LaundrycheckDbHelper.COLUMN_PICTURE,
            LaundrycheckDbHelper.COLUMN_WASHCOUNT,
            LaundrycheckDbHelper.COLUMN_LAST_WASHED,
            LaundrycheckDbHelper.COLUMN_PIECES,
            LaundrycheckDbHelper.COLUMN_CLOTHESTYPE
    };

/*
    Db Helper gets created
 */
    public ClothesDataSource(Context context) {
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


//    /*
//    Insert a new Washorder into the DB and return the insered as Washorder
//     */
//    public Clothes createClothes(String name) {
//
//        ContentValues values = new ContentValues();
//        values.put(LaundrycheckDbHelper.COLUMN_NAME, name);
//        values.put(LaundrycheckDbHelper.COLUMN_RFID_ID, "");
//        values.put(LaundrycheckDbHelper.COLUMN_PICTURE, "");
//        values.put(LaundrycheckDbHelper.COLUMN_PICKUP_DATE, "");
//        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_COUNT, 0);
//        values.put(LaundrycheckDbHelper.COLUMN_PRICE, 0);
//        values.put(LaundrycheckDbHelper.COLUMN_COMMENTS, "");
//
//
//        long insertId = database.insert(LaundrycheckDbHelper.TABLE_WASH_ORDERS, null, values);
//
//        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                washorder_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + insertId,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        Washorder washorder = cursorToWashorder(cursor);
//        cursor.close();
//
//        return washorder;
//    }
//
//    /*
//     * Get the washorder by Id
//     */
//    public Washorder getWashorder(long Id) {
//
//        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                washorder_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + Id,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        Washorder washorder = cursorToWashorder(cursor);
//        cursor.close();
//
//        return washorder;
//    }
//
//
//    private Washorder cursorToWashorder(Cursor cursor) {
//        int idIndex = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ID);
//        int idName = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_NAME);
//        int idAddress = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ADDRESS);
//        int idDeliveryDate = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_DELIVERY_DATE);
//        int idPickupDate = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PICKUP_DATE);
//        int idClothesCount = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_CLOTHES_COUNT);
//        int idPrice = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PRICE);
//        int idComments = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_COMMENTS);
//
//        String name = cursor.getString(idName);
//        String address = cursor.getString(idAddress);
//        String deliverydate = cursor.getString(idDeliveryDate);
//        String pickupdate = cursor.getString(idPickupDate);
//        String comments = cursor.getString(idComments);
//        int clothescount = cursor.getInt(idClothesCount);
//        int price = cursor.getInt(idPrice);
//        long id = cursor.getLong(idIndex);
//
//        Washorder washorder = new Washorder();
//        washorder.setId(id);
//        washorder.setAddress(address);
//        washorder.setDelivery_date(deliverydate);
//        washorder.setPickup_date(pickupdate);
//        washorder.setClothes_count(clothescount);
//        washorder.setComments(comments);
//        washorder.setName(name);
//        washorder.setPrice(price);
//
//        return washorder;
//    }
//
//
//    public List<Washorder> getAllWashorders() {
//        List<Washorder> washorderslist = new ArrayList<>();
//
//        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                washorder_columns, null, null, null, null, LaundrycheckDbHelper.COLUMN_ID +" DESC");
//
//        cursor.moveToFirst();
//        Washorder washorder;
//
//        while(!cursor.isAfterLast()) {
//            washorder = cursorToWashorder(cursor);
//            washorderslist.add(washorder);
//            Log.d(LOG_TAG, "ID: " + washorder.getId() + ", Inhalt: " + washorder.toString());
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//
//        return washorderslist;
//    }
//
//    /*
//     * Deletes the specific washorder form the DB
//     */
//    public void deleteWashorder(Washorder curWashorder){
//        long id = curWashorder.getId();
//
//        database.delete(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                LaundrycheckDbHelper.COLUMN_ID + "=" + id,
//                null);
//
//        Log.d(LOG_TAG, "Washorder deletet! ID: " + id);
//
//    }
//
//    /*
//     * Update an existing Washorder in the DB
//     */
//    public Washorder updateWashorder(Washorder changedWashorder) {
//        ContentValues values = new ContentValues();
//        values.put(LaundrycheckDbHelper.COLUMN_NAME, changedWashorder.getName());
//        values.put(LaundrycheckDbHelper.COLUMN_ADDRESS, changedWashorder.getAddress());
//        values.put(LaundrycheckDbHelper.COLUMN_DELIVERY_DATE, changedWashorder.getDelivery_date());
//        values.put(LaundrycheckDbHelper.COLUMN_PICKUP_DATE, changedWashorder.getPickup_date());
//        values.put(LaundrycheckDbHelper.COLUMN_PRICE, changedWashorder.getPrice());
//        values.put(LaundrycheckDbHelper.COLUMN_COMMENTS, changedWashorder.getComments());
//
//        database.update(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                values,
//                LaundrycheckDbHelper.COLUMN_ID + "=" + changedWashorder.getId(),
//                null);
//
//        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_WASH_ORDERS,
//                washorder_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + changedWashorder.getId(),
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        Washorder washorder = cursorToWashorder(cursor);
//        cursor.close();
//
//        return washorder;
//    }
//
}
