package com.example.user.sqliteproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class DB {


    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "SHOPINGLIST";

    private static String nameOfTable = "Example";

    public static String getNameOfTable() {
        return nameOfTable;
    }

    public static void setNameOfTable(String nameOfTable) {
        DB.nameOfTable = nameOfTable;
    }

    static final String KEY_ID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_COST = "cost";

    DB(Context ctx) {
        context = ctx;
    }

    private DBHelper dbHelper;
    public static SQLiteDatabase database;


    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    Cursor getAllData() {
        return database.query(nameOfTable, null, null, null, null, null, null);
    }

    void addRec(String name) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        database.insert(nameOfTable, null, cv);
    }

    void delRec(long id) {
        database.delete(nameOfTable, KEY_ID + " = " + id, null);
    }

    void deleteTable(String tabl) {

        database.execSQL("DROP TABLE " + tabl);


    }

    void close() {
        if (dbHelper != null) dbHelper.close();
    }
}