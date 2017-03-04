package com.example.user.sqliteproj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "listbuyDB";
    public static final String TABLE_LISTBUY = "listbuy";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_KIND = "kind";
    public static final String KEY_COST = "cost";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LISTBUY + "("
                + KEY_ID + " integer primary key,"
                + KEY_NAME + " text,"
                + KEY_PRICE + " double,"
                + KEY_QUANTITY + " double,"
                + KEY_KIND + " integer,"
                + KEY_COST + " double" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_LISTBUY);
        onCreate(db);

    }
}

