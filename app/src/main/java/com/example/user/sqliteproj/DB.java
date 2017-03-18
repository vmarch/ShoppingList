package com.example.user.sqliteproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DB {
    private final Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "listbuyDB";
    public static final String TABLE_LISTBUY = "listbuy";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_KIND = "kind";
    public static final String KEY_COST = "cost";


    protected static final String DB_CREATE =
            "create table " + TABLE_LISTBUY + "("
                    + KEY_ID + " integer primary key autoincrement,"
                    + KEY_NAME + " text,"
                    + KEY_PRICE + " double,"
                    + KEY_QUANTITY + " double,"
                    + KEY_KIND + " integer,"
                    + KEY_COST + " double" + ")";

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DB(Context ctx) {
        context = ctx;
    }

    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null) dbHelper.close();
    }

    // получить все данные из таблицы TABLE_LISTBUY
    public Cursor getAllData() {
        return database.query(TABLE_LISTBUY, null, null, null, null, null, null);
    }

    public void addRec(String name, String price, String quantity, String kind) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_KIND, kind);
        database.insert(TABLE_LISTBUY, null, cv);
    }

    public void delRec(long id) {
        database.delete(TABLE_LISTBUY, KEY_ID + " = " + id, null);
    }

    public void delAllRec() {
        database.delete(TABLE_LISTBUY, null, null);
    }
}

