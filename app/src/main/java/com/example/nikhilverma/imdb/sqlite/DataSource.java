package com.example.nikhilverma.imdb.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.nikhilverma.imdb.Activites.MainActivity;
import com.example.nikhilverma.imdb.Models.SqliteModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 02-01-2015.
 */
public class DataSource {
    //Logcat Tag
    private static final String LOG_CAT = "Sqlite";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    private static SQLiteDatabase sqLiteDatabase;

    public DataSource(Context context) {
        sqLiteOpenHelper = new SqliteHelper(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

    public void create(SqliteModel model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COLUMN_TITLE, model.getTITLE());
        contentValues.put(SqliteHelper.COLUMN_IMAGE_URL, model.getIMAGE_URL());
        contentValues.put(SqliteHelper.COLUMN_RATING, model.getRATING());
        contentValues.put(SqliteHelper.COLUMN_YEAR, model.getYEAR());
        sqLiteDatabase.insert(SqliteHelper.TABLE_MOVIE, null, contentValues);
        close();
    }

    public SqliteModel get(int id) {
        SqliteModel model = null;
        String Query = "SELECT  * FROM " + SqliteHelper.TABLE_MOVIE + " WHERE " + SqliteHelper.COLUMN_ID + "=" + id + ";";
        Log.i(LOG_CAT, Query);
        if (!sqLiteDatabase.isOpen())
            open();

        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        Log.i(LOG_CAT, "1");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                Log.i(LOG_CAT, "2");
                model.setID(cursor.getColumnIndex(SqliteHelper.COLUMN_ID));
                Log.i(LOG_CAT, "3");
                model.setTITLE(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_TITLE)));
                Log.i(LOG_CAT, "4");
                model.setIMAGE_URL(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_IMAGE_URL)));
                Log.i(LOG_CAT, "5");
                model.setRATING(cursor.getFloat(cursor.getColumnIndex(SqliteHelper.COLUMN_RATING)));
                Log.i(LOG_CAT, "6");
                model.setYEAR(cursor.getInt(cursor.getColumnIndex(SqliteHelper.COLUMN_YEAR)));
                close();
            } catch (Exception e) {
                Log.i(LOG_CAT, e.toString());
            }
        }
        return model;
    }

    public List<SqliteModel> getAllMovies() {
        List<SqliteModel> movielist = new ArrayList<SqliteModel>();
        String Query = "SELECT  * FROM " + SqliteHelper.TABLE_MOVIE;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                SqliteModel m = new SqliteModel();
                m.setID(Integer.parseInt(cursor.getString(0)));
                m.setTITLE(cursor.getString(1));
                m.setIMAGE_URL(cursor.getString(2));
                m.setRATING(cursor.getFloat(3));
                m.setYEAR(cursor.getInt(4));
                movielist.add(m);
            } while (cursor.moveToNext());
        }
        close();
        if (movielist == null) {
            Toast.makeText(MainActivity.context, "Sqlite Database Is Null", Toast.LENGTH_LONG).show();
        }
        return movielist;
    }

    public int getMoviesCount() {
        String countQuery = "SELECT  * FROM " + SqliteHelper.TABLE_MOVIE;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        Log.i("Count.Sqlite.Nikhil", d + "");
        return d;
    }
}
