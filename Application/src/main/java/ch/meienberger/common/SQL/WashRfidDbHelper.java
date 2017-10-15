package ch.meienberger.common.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
    Handles the creation and update of the Db and can open the connection to an existing Db
 */

public class WashRfidDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Wash_Rfid_Database";


    public WashRfidDbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        Log.d(LOG_TAG, "DbHelper created the DB: " + DB_NAME);
    }

    private static final String LOG_TAG = WashRfidDbHelper.class.getSimpleName();


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
