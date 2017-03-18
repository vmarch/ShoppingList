package com.example.user.sqliteproj;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 3;
    ListView lv_list;

    Button btnAdd, btnEmpty, btnRead, btnClear;
    TextView tvNameId, tvNameName, tvNamePrice, tvNameQuantity, tvNameKind, tvNameCost;
    EditText tvName, tvPrice, tvQuantity, tvKind;

    DB db;
    DBHelper dbHelper;
    SimpleCursorAdapter scAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        db.open();

        // формируем столбцы сопоставления
        String[] from = new String[]{DB.KEY_ID, DB.KEY_NAME, DB.KEY_PRICE, DB.KEY_QUANTITY, DB.KEY_KIND, DB.KEY_COST};
        int[] to = new int[]{R.id.tvListId, R.id.tvListName, R.id.tvListPrice, R.id.tvListQuantity, R.id.tvListKind, R.id.tvListCost};

        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setAdapter(scAdapter);


        // добавляем контекстное меню к списку
        registerForContextMenu(lv_list);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);


        tvNameId = (TextView) findViewById(R.id.tvNameId);
        tvNameName = (TextView) findViewById(R.id.tvNameName);
        tvNamePrice = (TextView) findViewById(R.id.tvNamePrice);
        tvNameQuantity = (TextView) findViewById(R.id.tvNameQuantity);
        tvNameKind = (TextView) findViewById(R.id.tvNameKind);
        tvNameCost = (TextView) findViewById(R.id.tvNameCost);

        tvName = (EditText) findViewById(R.id.tvName);
        tvPrice = (EditText) findViewById(R.id.tvPrice);
        tvQuantity = (EditText) findViewById(R.id.tvQuantity);
        tvKind = (EditText) findViewById(R.id.tvKind);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnEmpty = (Button) findViewById(R.id.btnEmpty);
        btnEmpty.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString();
        String quantity = tvQuantity.getText().toString();
        String kind = tvKind.getText().toString();

        switch (view.getId()) {
            case R.id.btnAdd:
                if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Fill all fields", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    db.addRec(name, price, quantity, kind);
                    getSupportLoaderManager().getLoader(0).forceLoad();
//                    tvName.setText("");
//                    tvPrice.setText("");
//                    tvQuantity.setText("");
//                    tvKind.setText("");
                }
                break;

            case R.id.btnEmpty:
                tvName.setText("");
                tvPrice.setText("");
                tvQuantity.setText("");
                tvKind.setText("");
                break;

            case R.id.btnRead:
                getSupportLoaderManager().getLoader(0).forceLoad();
                break;

            case R.id.btnClear:
                db.delAllRec();
                getSupportLoaderManager().getLoader(0).forceLoad();
                break;
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRec(acmi.id);
            // получаем новый курсор с данными
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
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

    static class MyCursorLoader extends CursorLoader {
        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            return cursor;
        }

    }


}


