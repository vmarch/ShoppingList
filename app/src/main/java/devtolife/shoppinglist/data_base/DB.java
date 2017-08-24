package devtolife.shoppinglist.data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CHECKED = "checked";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_IMPORTANT = "important";
    private static final int DATABASE_VERSION = 3;
    public static SQLiteDatabase database;
    private static String DATABASE_NAME = "SHOPINGLIST";
    private static String nameOfTable = "Example";
    private Context context;
    private DBHelper dbHelper;


    public DB(Context ctx) {
        context = ctx;
    }

    public static String getNameOfTable() {
        return nameOfTable;
    }

    public static void setNameOfTable(String nameOfTable) {
        DB.nameOfTable = nameOfTable;
    }

    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public void upDateCheck(long id, int checked) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CHECKED, checked);
        database.update("\'" + getNameOfTable() + "\'", cv, "_id = " + id, null);
    }

    void upDateRec(long id, String name, double price, int quantity, int important) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        database.update("\'" + getNameOfTable() + "\'", cv, "_id = " + id, null);
    }

    public void upDateName(long id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        database.update("\'" + getNameOfTable() + "\'", cv, "_id = " + id, null);
    }

    public Cursor getAllData() {
        return database.query("\'" + getNameOfTable() + "\'", null, null, null, null, null, "checked");
    }
    public Cursor getAllItemForShare() {
        return database.query("\'" + getNameOfTable() + "\'", null, null, null, null, null, null);
    }

    public void addRec(String name, int checked, double price, int quantity, int important) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_CHECKED, checked);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_QUANTITY, quantity);
        cv.put(KEY_IMPORTANT, important);

        database.insert("\'" + getNameOfTable() + "\'", null, cv);
    }

    public void delRec(long id) {
        database.delete("\'" + getNameOfTable() + "\'", KEY_ID + " = " + id, null);
    }

    public Cursor toGetCheckedItem(long id) {
        return database.rawQuery("SELECT checked FROM " + "\'" + DB.getNameOfTable() + "\'" + " WHERE _id = " + id, null);
    }

    public void deleteTable(String tabl) {
        database.execSQL("DROP TABLE " + "\'" + tabl + "\'");
    }

    public void close() {
        if (dbHelper != null) dbHelper.close();
    }
}