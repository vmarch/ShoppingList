package devtolife.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ProdActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DEBUG_TAG = "Gecture";
    DB db;
    DBHelper dbHelper;
    SimpleCursorAdapter scAdapter;
    Context context;
    String name;

    private View onTouchedItemView;

    private ListView lv_list;
    private Button btnAdd;
    private EditText tvName;
    private SharedPreferences mSharedPref;
    private int pointDownX;
    private int pointDownY;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setTitle("" + DB.getNameOfTable());
        } catch (Exception e) {
        }

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.KEY_NAME};
        int[] to = new int[]{R.id.tv_list_name};

        scAdapter = new MySCA(this, R.layout.prod_item, null, from, to, 0);

        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setAdapter(scAdapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                TextView txt = (TextView) v;

                Cursor c = db.toGetCheckedItem(id);
                c.moveToFirst();

                int checked = c.getInt(c.getColumnIndex("checked"));

                Toast.makeText(getApplicationContext(),
                        "checked:  " + checked, Toast.LENGTH_SHORT).show();

                toCheckProd(id, v, txt, checked);


//                TODO revert strike

            }
        });

        registerForContextMenu(lv_list);

        getSupportLoaderManager().initLoader(0, null, this);

        tvName = (EditText) findViewById(R.id.tv_name);
        tvName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    createNewItem();
                    return true;

                } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    createNewItem();
                    return true;

                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    createNewItem();
                    return true;
                }
                return false;
            }
        });

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewItem();
            }
        });
        db.close();
    }

    public class MySCA extends SimpleCursorAdapter {

        public MySCA(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            int listCursor = cursor.getInt(cursor.getColumnIndex("checked"));

            TextView tv = (TextView) view;

            cursor.moveToFirst();

            Toast.makeText(getApplicationContext(),
                    "c2: " + listCursor, Toast.LENGTH_SHORT).show();

            if (cursor.getInt(cursor.getColumnIndex("checked")) == 1) {
//                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                tv.setTextColor(getResources().getColor(R.color.colorCheckedText));
//                view.setBackgroundColor(getResources().getColor(R.color.colorCheckedItem));

            } else {
                tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tv.setTextColor(getResources().getColor(R.color.colorOfText));
                view.setBackgroundColor(getResources().getColor(R.color.colorOfItem));

            }
        }

    }
//        @Override
//        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//
//            TextView tv = (TextView) view;
//
//            cursor.moveToFirst();
//
//            Toast.makeText(getApplicationContext(),
//                    "c2: " + cursor.getInt(cursor.getColumnIndex("checked")), Toast.LENGTH_SHORT).show();
//
//            if (cursor.getInt(cursor.getColumnIndex("checked")) == 1) {
////                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
////                tv.setTextColor(getResources().getColor(R.color.colorCheckedText));
//                view.setBackgroundColor(getResources().getColor(R.color.colorCheckedItem));
//                return true;
//
//            } else if (cursor.getInt(cursor.getColumnIndex("checked")) == 0) {
//                tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                tv.setTextColor(getResources().getColor(R.color.colorOfText));
//                view.setBackgroundColor(getResources().getColor(R.color.colorOfItem));
//                return true;
//
//            } else {
//                return false;
//            }
//
//        }
//    }

    public void toCheckProd(long id, View v, TextView tv, int check) {

        if (check == 0) {

            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setTextColor(getResources().getColor(R.color.colorCheckedText));
            v.setBackgroundColor(getResources().getColor(R.color.colorCheckedItem));
            db.upDateCheck(id, 1);

        } else if (check == 1) {

            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setTextColor(getResources().getColor(R.color.colorOfText));
            v.setBackgroundColor(getResources().getColor(R.color.colorOfItem));
            db.upDateCheck(id, 0);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prod_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long itemIdInContextMenu = scAdapter.getItemId(info.position);

        switch (item.getItemId()) {

            case R.id.delete_prod:
                db = new DB(this);
                db.open();
                db.delRec(itemIdInContextMenu);
                getSupportLoaderManager().initLoader(0, null, ProdActivity.this);
                db.close();
                return true;

//            case R.id.edit_prod:
////               TODO edit-menu
//                String edName = "kkk";
//                int edChecked = 0;
//                double edPrice = 0.0;
//                int edQuantity = 1;
//                int edImportant = 1;
//
//                db = new DB(this);
//                db.open();
//                db.upDateRec(edName, edChecked, edPrice, edQuantity, edImportant);
//                db.close();
//                getSupportLoaderManager().initLoader(0, null, this);
//                return true;
//
//            case R.id.to_favorite:
////                TODO add to favorite
////                db = new DB(this);
////                db.open();
////
////                String oldTableName = DB.getNameOfTable();
////
////                Cursor cTable = DB.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
////
////                if (cTable.moveToFirst()) {
////
////                    while (!cTable.isAfterLast()) {
////                        cTable.getString(0);
//////TODO check for exist
////                        if (!cTable.getString(0).equals(getNewTableName() + "(copy)")) {
////                            setNewTableName(DB.getNameOfTable() + "(copy)");
////
////                        } else if (cTable.getString(0).equals(getNewTableName() + "(copy)")) {
////
////                            cTable.close();
////                            db.close();
////                            DB.setNameOfTable(oldTableName);
////                            updateAdapter();
////                            return super.onContextItemSelected(item);
////
////                        } else {
////                            break;
////                        }
////                        cTable.moveToNext();
////                    }
////                }
////                DB.setNameOfTable(getNewTableName());
////
////                DB.database.execSQL("create table " + "\'" + DB.getNameOfTable() + "\'" + "("
////                        + DB.KEY_ID + " integer primary key autoincrement,"
////                        + DB.KEY_NAME + " TEXT" + ")");
////
////                Cursor cList = DB.database.query("\'" + oldTableName + "\'", null, null, null, null, null, null);
////
////                if (cList.moveToFirst()) {
////                    while (!cList.isAfterLast()) {
////                        cList.getString(1);
////                        db.addRec(cList.getString(1));
////                        cList.moveToNext();
////                    }
////                }
////                cList.close();
////                db.close();
////
////                Intent intent = new Intent(this, ProdActivity.class);
////                startActivity(intent);
////
////                return true;
//                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    private View getViewByPosition(int pos, ListView lv) {
        final int firstListItemPosition = lv.getFirstVisiblePosition();

        final int childIndex = pos - firstListItemPosition;
        return lv.getChildAt(childIndex);
    }

    private static class MyCursorLoader extends CursorLoader {
        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            db = new DB(getContext());
            db.open();
            return db.getAllData();

        }
    }


    private void createNewItem() {
        //        TODO filling of items
        name = tvName.getText().toString();
        int checked = 0;
        double price = 0.0;
        int quantity = 1;
        int important = 1;

        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            db = new DB(this);
            db.open();
            db.addRec(name, checked, price, quantity, important);
            getSupportLoaderManager().getLoader(0).forceLoad();
            db.close();
            tvName.setText("");
        }

    }


    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}


