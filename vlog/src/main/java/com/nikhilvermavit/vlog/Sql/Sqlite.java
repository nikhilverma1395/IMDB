package com.nikhilvermavit.vlog.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 2/5/2015.
 */
public class Sqlite extends SQLiteOpenHelper {
    public static final String TABLE_VOLSBB = "Multiple";
    public static final String _ID = "_id";
    public static final String COLUMN_USERNAME = "_username_";
    public static final String COLUMN_PASSWORD = "_password_";
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_VOLSBB + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT " + ")";
    //Logcat Tag
    private static final String LOG_CAT = "Sqlite.Volsbb";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.Volsbb";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    private static Context context = null;


    public Sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLSBB);
    }
}
