package com.example.user.sqliteproj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
             int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + DB.getNameOfTable() + "("
                + DB.KEY_ID + " integer primary key autoincrement,"
                + DB.KEY_NAME + " text,"
                + DB.KEY_COST + " double" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + DB.getNameOfTable());
        onCreate(db);
    }
}
