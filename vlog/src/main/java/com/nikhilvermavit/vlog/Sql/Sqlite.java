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
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_RENEW_DATE = "rndate";
    public static final String COLUMN_PASSWORD = "passwordUse";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_VOLSBB + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + "   TEXT, " +
            COLUMN_PASSWORD + "   TEXT, " +
            COLUMN_RENEW_DATE + " TEXT " + ")";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.Volsbb";
    //Database Version
    private static final int DATABASE_VERSION = 1;


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
