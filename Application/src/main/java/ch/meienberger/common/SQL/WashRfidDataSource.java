package ch.meienberger.common.SQL;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WashRfidDataSource {

    private static final String LOG_TAG = WashRfidDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private WashRfidDbHelper dbHelper;


    public WashRfidDataSource(Context context) {
        Log.d(LOG_TAG, "Datasource creates the dbHelper.");
        dbHelper = new WashRfidDbHelper(context);
    }
}
