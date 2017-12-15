package ch.meienberger.android.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
    Handles the creation and update of the Db and can open the connection to an existing Db
 */

public class LaundrycheckDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "laundry_check.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_WASH_ORDERS = "wash_orders";
    public static final String TABLE_CLOTHES = "clothes";
    public static final String TABLE_MAPPING = "mapping";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RFID_ID = "rfid_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DELIVERY_DATE = "delivery_date";
    public static final String COLUMN_PICKUP_DATE = "pickup_date";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CLOTHES_COUNT = "clothes_count";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_WASHCOUNT = "washcount";
    public static final String COLUMN_LAST_WASHED = "last_washed";
    public static final String COLUMN_PIECES = "pieces";
    public static final String COLUMN_CLOTHESTYPE = "clothestype";
    public static final String COLUMN_CLOTHES_ID = "clothes_id";
    public static final String COLUMN_WASHORDER_ID = "washorder_id";
    public static final String COLUMN_CLOTHES_IS_RETURNED = "clothes_is_returned";


    public static final String SQL_CREATE_TABLE_WASHORDERS =
            "CREATE TABLE " + TABLE_WASH_ORDERS +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_DELIVERY_DATE + " TEXT NOT NULL, " +
                    COLUMN_PICKUP_DATE + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " INTEGER NOT NULL, " +
                    COLUMN_COMMENTS + " TEXT NOT NULL, " +
                    COLUMN_CLOTHES_COUNT + " INTEGER NOT NULL);";

    public static final String SQL_CREATE_TABLE_CLOTHES =
            "CREATE TABLE " + TABLE_CLOTHES +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_RFID_ID + " TEXT NOT NULL, " +
                    COLUMN_PICTURE + " TEXT NOT NULL, " +
                    COLUMN_WASHCOUNT + " INTEGER NOT NULL, " +
                    COLUMN_LAST_WASHED + " TEXT NOT NULL, " +
                    COLUMN_PIECES + " INTEGER NOT NULL, " +
                    COLUMN_CLOTHESTYPE + " INTEGER NOT NULL);";

    public static final String SQL_CREATE_TABLE_MAPPING =
            "CREATE TABLE " + TABLE_MAPPING +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WASHORDER_ID + " INTEGER NOT NULL, " +
                    COLUMN_CLOTHES_ID + " INTEGER NOT NULL, " +
                    COLUMN_CLOTHES_IS_RETURNED + " BIT NOT NULL);";


    public LaundrycheckDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper created the DB: " + DB_NAME);
    }

    private static final String LOG_TAG = LaundrycheckDbHelper.class.getSimpleName();


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d(LOG_TAG, "Table WASHORDERS is going to be created by command: " + SQL_CREATE_TABLE_WASHORDERS);
            sqLiteDatabase.execSQL(SQL_CREATE_TABLE_WASHORDERS);

            Log.d(LOG_TAG, "Table CLOTHES is going to be created by command: " + SQL_CREATE_TABLE_CLOTHES);
            sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CLOTHES);

            Log.d(LOG_TAG, "Table MAPPING is going to be created by command: " + SQL_CREATE_TABLE_MAPPING);
            sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MAPPING);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fault while creating table: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
