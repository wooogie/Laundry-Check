package ch.meienberger.android.SQL;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;


public class ClothesDataSource {

    private static final String LOG_TAG = ClothesDataSource.class.getSimpleName();

    protected SQLiteDatabase database;
    protected LaundrycheckDbHelper dbHelper;
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


    /*
    Insert a new Clothes into the DB and return the insered as Clothes object.
     */
    public Clothes createClothes(String name) {

        ContentValues values = new ContentValues();
        values.put(LaundrycheckDbHelper.COLUMN_NAME, name);
        values.put(LaundrycheckDbHelper.COLUMN_RFID_ID, "");
        values.put(LaundrycheckDbHelper.COLUMN_PICTURE, "");
        values.put(LaundrycheckDbHelper.COLUMN_WASHCOUNT, 0);
        values.put(LaundrycheckDbHelper.COLUMN_LAST_WASHED, "");
        values.put(LaundrycheckDbHelper.COLUMN_PIECES, 0);
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHESTYPE, 0);


        long insertId = database.insert(LaundrycheckDbHelper.TABLE_CLOTHES, null, values);

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_CLOTHES,
                clothes_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Clothes clothes = cursorToClothes(cursor);
        cursor.close();

        return clothes;
    }

    /*
     * Get the clothes by Id
     */
    public Clothes getClothes(long Id) {

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_CLOTHES,
                clothes_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + Id,
                null, null, null, null);

        cursor.moveToFirst();
        Clothes clothes = cursorToClothes(cursor);
        cursor.close();

        return clothes;
    }


    private Clothes cursorToClothes(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_NAME);
        int idRfidId = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_RFID_ID);
        int idPicture = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PICTURE);
        int idWashcount = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_WASHCOUNT);
        int idLastWashed = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_LAST_WASHED);
        int idPieces = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_PIECES);
        int idClothestype = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_CLOTHESTYPE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        String rfid_id = cursor.getString(idRfidId);
        String picture = cursor.getString(idPicture);
        int washcount = cursor.getInt(idWashcount);
        String lastwashed = cursor.getString(idLastWashed);
        int pieces = cursor.getInt(idPieces);
        int int_clothestype = cursor.getInt(idClothestype);

        Clothes clothes = new Clothes();
        clothes.setId(id);
        clothes.setName(name);
        clothes.setRfid_id(rfid_id);
        clothes.setPicture(picture);
        clothes.setWashcount(washcount);
        clothes.setLast_washed(lastwashed);
        clothes.setPieces(pieces);

        Clothes.Clothestype c = Clothes.Clothestype.Others.getValue(int_clothestype);
        clothes.setClothestype(c);

        return clothes;
    }


    public List<Clothes> getAllClothes() {
        List<Clothes> clotheslist = new ArrayList<>();

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_CLOTHES,
                clothes_columns, null, null, null, null, LaundrycheckDbHelper.COLUMN_ID +" DESC");

        cursor.moveToFirst();
        Clothes clothes;

        while(!cursor.isAfterLast()) {
            clothes = cursorToClothes(cursor);
            clotheslist.add(clothes);
            Log.d(LOG_TAG, "ID: " + clothes.getId() + ", Inhalt: " + clothes.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return clotheslist;
    }

    /*
     * Deletes the specific washorder form the DB and all references.
     */
    public void deleteClothes(Clothes curClothes){
        long id = curClothes.getId();

        //delete clothes
        database.delete(LaundrycheckDbHelper.TABLE_CLOTHES,
                LaundrycheckDbHelper.COLUMN_ID + "=" + id,
                null);


        //delete mapping
        database.delete(LaundrycheckDbHelper.TABLE_MAPPING,
                LaundrycheckDbHelper.COLUMN_CLOTHES_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Clothes deletet! ID: " + id);

    }

    /*
     * Update an existing Clothes in the DB
     */
    public Clothes updateClothes(Clothes changedClothes) {
        ContentValues values = new ContentValues();
        values.put(LaundrycheckDbHelper.COLUMN_NAME, changedClothes.getName());
        values.put(LaundrycheckDbHelper.COLUMN_RFID_ID, changedClothes.getRfid_id());
        values.put(LaundrycheckDbHelper.COLUMN_PICTURE, changedClothes.getPicturePath());
        values.put(LaundrycheckDbHelper.COLUMN_WASHCOUNT, changedClothes.getWashcount());
        values.put(LaundrycheckDbHelper.COLUMN_LAST_WASHED, changedClothes.getLast_washed());
        values.put(LaundrycheckDbHelper.COLUMN_PIECES, changedClothes.getPieces());
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHESTYPE, changedClothes.getClothestype().value);

        database.update(LaundrycheckDbHelper.TABLE_CLOTHES,
                values,
                LaundrycheckDbHelper.COLUMN_ID + "=" + changedClothes.getId(),
                null);

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_CLOTHES,
                clothes_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + changedClothes.getId(),
                null, null, null, null);

        cursor.moveToFirst();
        Clothes clothes = cursorToClothes(cursor);
        cursor.close();

        return clothes;
    }

}
