package ch.meienberger.android.SQL;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.meienberger.android.laundrycheck.custom_class_objects.Clothes;
import ch.meienberger.android.laundrycheck.custom_class_objects.Mapping;


public class MappingDataSource extends ClothesDataSource {

    private static final String LOG_TAG = MappingDataSource.class.getSimpleName();

    private String[] mapping_columns = {
            LaundrycheckDbHelper.COLUMN_ID,
            LaundrycheckDbHelper.COLUMN_CLOTHES_ID,
            LaundrycheckDbHelper.COLUMN_WASHORDER_ID,
            LaundrycheckDbHelper.COLUMN_CLOTHES_IS_RETURNED
    };

/*
    Db Helper gets created
 */
    public MappingDataSource(Context context) {
        super(context);
    }

    /*
    Db gets open as writable
     */
    public void open() {
        super.open();
    }

    /*
    Db gets closed
     */
    public void close() {
        super.close();
    }


    /*
    Insert a new Mapping into the DB and return the insered as Mapping object.
     */
    public Mapping createMapping(int Washorder_Id, int Clothes_Id) {

        ContentValues values = new ContentValues();
        values.put(LaundrycheckDbHelper.COLUMN_WASHORDER_ID, Washorder_Id);
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_ID, Clothes_Id);
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_IS_RETURNED, false);


        long insertId = database.insert(LaundrycheckDbHelper.TABLE_MAPPING, null, values);

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Mapping mapping = cursorToMapping(cursor);
        cursor.close();

        return mapping;
    }

    /*
     * Get the mapping by Id
     */
    public Mapping getMapping(long Id) {

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + Id,
                null, null, null, null);

        cursor.moveToFirst();
        Mapping mapping = cursorToMapping(cursor);
        cursor.close();

        return mapping;
    }

    /*
    * Returns, if the Clothes is already mapped to a specific washorder
    */
    public boolean ClothesIsAlreadyMapped(long Washorder_Id,long Clothes_Id) {
        List<Mapping> mappinglist = new ArrayList<>();

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, LaundrycheckDbHelper.COLUMN_WASHORDER_ID + "=" + Washorder_Id + " and "
                + LaundrycheckDbHelper.COLUMN_CLOTHES_ID + "=" + Clothes_Id,
                null, null, null, null);

        cursor.moveToFirst();
        if(cursor.getCount()==0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }


    private Mapping cursorToMapping(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_ID);
        int idWashorderId = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_WASHORDER_ID);
        int idClothesId = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_CLOTHES_ID);
        int idClothesIsReturned = cursor.getColumnIndex(LaundrycheckDbHelper.COLUMN_CLOTHES_IS_RETURNED);

        long id = cursor.getLong(idIndex);
        long washorder_id = cursor.getLong(idWashorderId);
        long clothes_id = cursor.getLong(idClothesId);
        boolean clothesisreturned;

        //// TODO: 04.12.2017
        if (cursor.getInt(idClothesIsReturned) == 0){
            clothesisreturned = false;
        }else{
            clothesisreturned = true;
        }

        Mapping mapping = new Mapping(washorder_id,clothes_id);
        mapping.setClothes_returned(clothesisreturned);

        mapping.setId(id);


        return mapping;
    }


    public List<Mapping> getAllMapping() {
        List<Mapping> mappinglist = new ArrayList<>();

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, null, null, null, null, LaundrycheckDbHelper.COLUMN_ID +" DESC");

        cursor.moveToFirst();
        Mapping mapping;

        while(!cursor.isAfterLast()) {
            mapping = cursorToMapping(cursor);
            mappinglist.add(mapping);
            Log.d(LOG_TAG, "ID: " + mapping.getId() + ", Inhalt: " + mapping.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return mappinglist;
    }




    public List<Mapping> getMappingfromWashorder(long Washorder_Id) {
        List<Mapping> mappinglist = new ArrayList<>();

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, LaundrycheckDbHelper.COLUMN_WASHORDER_ID + "=" + Washorder_Id, null, null, null, LaundrycheckDbHelper.COLUMN_ID +" DESC");

        cursor.moveToFirst();
        Mapping mapping;

        while(!cursor.isAfterLast()) {
            mapping = cursorToMapping(cursor);
            mappinglist.add(mapping);
            Log.d(LOG_TAG, "ID: " + mapping.getId() + ", Inhalt: " + mapping.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return mappinglist;
    }



    /*
     * Deletes the specific mapping form the DB
     */
    public void deleteMapping(Mapping curMapping){
        long id = curMapping.getId();

        database.delete(LaundrycheckDbHelper.TABLE_MAPPING,
                LaundrycheckDbHelper.COLUMN_ID + "=" + id,
                null);

        Log.d(LOG_TAG, "Mapping deletet! ID: " + id);

    }

    /*
     * Update an existing Mapping in the DB
     */
    public Mapping updateMapping(Mapping changedMapping) {
        ContentValues values = new ContentValues();
        values.put(LaundrycheckDbHelper.COLUMN_WASHORDER_ID, changedMapping.getWashorder_id());
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_ID, changedMapping.getClothes_id());
        values.put(LaundrycheckDbHelper.COLUMN_CLOTHES_IS_RETURNED, changedMapping.isClothes_returned());

        database.update(LaundrycheckDbHelper.TABLE_MAPPING,
                values,
                LaundrycheckDbHelper.COLUMN_ID + "=" + changedMapping.getId(),
                null);

        Cursor cursor = database.query(LaundrycheckDbHelper.TABLE_MAPPING,
                mapping_columns, LaundrycheckDbHelper.COLUMN_ID + "=" + changedMapping.getId(),
                null, null, null, null);

        cursor.moveToFirst();
        Mapping mapping = cursorToMapping(cursor);
        cursor.close();

        return mapping;
    }

}
