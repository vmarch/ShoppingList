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

import java.util.regex.Pattern;

public class ProdList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    DB dbPL;
    SimpleCursorAdapter scAdapter;
    String name;
    private ListView lv_list;
    private Button btnAdd;
    private EditText tvName;
    private SharedPreferences mSharedPref;
    private static long itemIdInContextMenu;

    public static long getItemIdInContextMenu() {
        return itemIdInContextMenu;
    }

    public static void setItemIdInContextMenu(long itemIdInContextMenu) {
        ProdList.itemIdInContextMenu = itemIdInContextMenu;
    }

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

        dbPL = new DB(this);
        dbPL.open();

        String[] from = new String[]{DB.KEY_NAME};
        int[] to = new int[]{R.id.tv_list_name};

        scAdapter = new SimpleCursorAdapter(this, R.layout.prod_item, null, from, to, 0);
        scAdapter.setViewBinder(new MySCA());

        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setAdapter(scAdapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                TextView txt = (TextView) v;

                Cursor c = dbPL.toGetCheckedItem(id);
                c.moveToFirst();

                int checked = c.getInt(c.getColumnIndex("checked"));

                toCheckProd(id, v, txt, checked);
                c.close();
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

        dbPL.close();
    }

    public void toCheckProd(long id, View v, TextView tv, int check) {

        if (check == 0) {

            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setTextColor(getResources().getColor(R.color.colorCheckedText));
            v.setBackgroundColor(getResources().getColor(R.color.colorCheckedItem));
            dbPL.upDateCheck(id, 1);
            getSupportLoaderManager().getLoader(0).forceLoad();

        } else if (check == 1) {

            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setTextColor(getResources().getColor(R.color.colorOfText));
            v.setBackgroundColor(getResources().getColor(R.color.colorOfItem));
            dbPL.upDateCheck(id, 0);
            getSupportLoaderManager().getLoader(0).forceLoad();
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
        setItemIdInContextMenu(scAdapter.getItemId(info.position));

        switch (item.getItemId()) {

            case R.id.delete_prod:
                dbPL = new DB(this);
                dbPL.open();
                dbPL.delRec(getItemIdInContextMenu());
                getSupportLoaderManager().getLoader(0).forceLoad();
                dbPL.close();
                return true;

            case R.id.edit_prod:
                String edName = ((TextView) info.targetView).getText().toString();
                EditItem.setOldText(edName);
                Intent intent = new Intent(this, EditItem.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbPL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void createNewItem() {
        //        TODO filling of items
        name = tvName.getText().toString();

        if (name.isEmpty()) {

            Toast.makeText(getApplicationContext(),
                    "Введіть завдання!", Toast.LENGTH_SHORT).show();

        } else if (!name.isEmpty() && Pattern.matches("(\\s)*", name + "")) {
            Toast.makeText(getApplicationContext(),
                    "Пуста назва. \nВведіть знову!", Toast.LENGTH_SHORT).show();
            tvName.setText("");

        } else {

            int checked = 0;
            double price = 0.0;
            int quantity = 1;
            int important = 1;

            dbPL = new DB(this);
            dbPL.open();
            dbPL.addRec(name, checked, price, quantity, important);
            getSupportLoaderManager().getLoader(0).forceLoad();
            dbPL.close();
            tvName.setText("");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    protected void onDestroy() {
        dbPL.close();
        super.onDestroy();
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

    private class MySCA implements SimpleCursorAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            int listCheckInfo = cursor.getInt(cursor.getColumnIndex("checked"));

            TextView tv = (TextView) view;

            switch (listCheckInfo) {
                case 1:
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tv.setTextColor(getResources().getColor(R.color.colorCheckedText));
                    view.setBackgroundColor(getResources().getColor(R.color.colorCheckedItem));
                    return false;

                case 0:
                    tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    tv.setTextColor(getResources().getColor(R.color.colorOfText));
                    view.setBackgroundColor(getResources().getColor(R.color.colorOfItem));
                    return false;
            }
            return false;

        }
    }
}


