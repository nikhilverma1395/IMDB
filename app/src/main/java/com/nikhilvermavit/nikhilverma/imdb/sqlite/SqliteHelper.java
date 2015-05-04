package com.nikhilvermavit.nikhilverma.imdb.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 01-01-2015.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    //Logcat Tag
    private static final String LOG_CAT = "Sqlite";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.IMDB";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    private static Context context = null;
    public static final String TABLE_MOVIE = "Recent";

    public static final String _ID = "_id";
    public static final String COLUMN_TITLE = "movieTitle";
    public static final String COLUMN_YEAR = "movieYear";
    public static final String COLUMN_IMAGE_URL = "movieImageUrl";
    public static final String COLUMN_RATING = "movieRating";
    public static final String COLUMN_IDs = "_id";
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_MOVIE + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_IMAGE_URL + " TEXT, " +
            COLUMN_RATING + " FLOAT,  " +
            COLUMN_YEAR +" TEXT "+ ")";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }
}
