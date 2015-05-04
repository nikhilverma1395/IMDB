package com.nikhilvermavit.vlog.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nikhilvermavit.vlog.SqlModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Verma on 2/5/2015.
 */
public class DataSource {
    private static final String LOG_CAT = "Sqlite.Volsbb";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public DataSource(Context context) {
        sqLiteOpenHelper = new Sqlite(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public List<SqlModel> getAllCredentials() {
        List<SqlModel> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + Sqlite.TABLE_VOLSBB;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                SqlModel m = new SqlModel();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setUsername(cursor.getString(1));
                m.setPassword(cursor.getString(2));
                list.add(m);
            } while (cursor.moveToNext());
        }
        close();
        try {
            if (list.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + Sqlite.TABLE_VOLSBB;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public boolean deleteFrompass(String url_id) {
        open();
        boolean fr = sqLiteDatabase.delete(Sqlite.TABLE_VOLSBB, Sqlite.COLUMN_USERNAME + "=?", new String[]{url_id}) > 0;
        close();
        return fr;
    }

    public void create(SqlModel model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Sqlite.COLUMN_USERNAME, model.getUsername());
        contentValues.put(Sqlite.COLUMN_PASSWORD, model.getPassword());
        long id = sqLiteDatabase.insert(Sqlite.TABLE_VOLSBB, null, contentValues);
        model.setId(id);
        close();
    }


    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }


}
