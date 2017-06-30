package devtolife.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListTables extends AppCompatActivity {

    private static final String DEBUG_TAG = "Gestures";
    private DB db;
    private ListView listTablesView;
    private Button tab;
    private List<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private View onTouchedItemView;
    private SharedPreferences mSharedPref;
    private String MYTHEME = "mytheme";
    private Intent intent;
//    private String newTableName;
//    public String getNewTableName() {
//        return newTableName;
//    }
//    public void setNewTableName(String newTableName) {
//        this.newTableName = newTableName;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt(MYTHEME, 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        tab = (Button) findViewById(R.id.btn_create);
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ListTables.this, CreateTable.class);
                startActivity(intent);
            }
        });
        listTablesView = (ListView) findViewById(R.id.list_table);
        registerForContextMenu(listTablesView);

        rawQuery();

        adapter = new ArrayAdapter<>(this, R.layout.list_item, arrayList);
        listTablesView.setAdapter(adapter);
        listTablesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onTouchedItemView = v;
                TextView tv = (TextView) onTouchedItemView;
                DB.setNameOfTable(tv.getText().toString());
                intent = new Intent(ListTables.this, ProdActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DB.setNameOfTable(adapter.getItem(info.position));

        switch (item.getItemId()) {

            case R.id.delete_list:
                db = new DB(this);
                db.open();
                db.deleteTable(DB.getNameOfTable());
                db.close();
                updateAdapter();
                return true;

//            case R.id.copy_list:
//                TODO Copy of LIST with changing of Name
//                db = new DB(this);
//                db.open();
//
//                String oldTableName = DB.getNameOfTable();
//
//                Cursor cTable = DB.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//                if (cTable.moveToFirst()) {
//
//                    while (!cTable.isAfterLast()) {
//                        cTable.getString(0);
////TODO check for exist
//                        if (!cTable.getString(0).equals(getNewTableName() + "(copy)")) {
//                            setNewTableName(DB.getNameOfTable() + "(copy)");
//
//                        } else if (cTable.getString(0).equals(getNewTableName() + "(copy)")) {
//
//                            cTable.close();
//                            db.close();
//                            DB.setNameOfTable(oldTableName);
//                            updateAdapter();
//                            return super.onContextItemSelected(item);
//
//                        } else {
//                            break;
//                        }
//                        cTable.moveToNext();
//                    }
//                }
//                cTable.close();
//                DB.setNameOfTable(getNewTableName());
//
//                DB.database.execSQL("create table " + "\'" + DB.getNameOfTable() + "\'" + "("
//                        + DB.KEY_ID + " integer primary key autoincrement,"
//                        + DB.KEY_NAME + " TEXT" + ")");
//
//                Cursor cList = DB.database.query("\'" + oldTableName + "\'", null, null, null, null, null, null);
//
//                if (cList.moveToFirst()) {
//                    while (!cList.isAfterLast()) {
//                        cList.getString(1);
//                        db.addRec(cList.getString(1),0,0.0,1,1);
//                        cList.moveToNext();
//                    }
//                }
//                cList.close();
//                db.close();
//
//                Intent intent = new Intent(this, ProdActivity.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent2;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = mSharedPref.edit();
        int idOfItem = item.getItemId();

        switch (idOfItem) {

            case R.id.action_green:
                ed.putInt("mytheme", R.style.AppThemeGreen);
                ed.apply();
                intent2 = getIntent();
                finish();
                startActivity(intent2);
                return true;

            case R.id.action_yellow:
                ed.putInt("mytheme", R.style.AppThemeYellow);
                ed.apply();
                intent2 = getIntent();
                finish();
                startActivity(intent2);
                return true;

            case R.id.action_blue:
                ed.putInt("mytheme", R.style.AppThemeBlue);
                ed.apply();
                intent2 = getIntent();
                finish();
                startActivity(intent2);
                return true;


            case R.id.action_grey:
                ed.putInt("mytheme", R.style.AppThemeGrey);
                ed.apply();
                intent2 = getIntent();
                finish();
                startActivity(intent2);
                return true;

            case R.id.action_pink:
                ed.putInt("mytheme", R.style.AppThemePink);
                ed.apply();
                intent2 = getIntent();
                finish();
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAdapter();
    }
//    private View getViewByPosition(int pos, ListView lv) {
//        final int firstListItemPosition = lv.getFirstVisiblePosition();
//        final int lastListItemPosition = firstListItemPosition + lv.getChildCount() - 1;
//
//        final int childIndex = pos - firstListItemPosition;
//        return lv.getChildAt(childIndex);
//    }

    private void updateAdapter() {
        adapter.clear();
        rawQuery();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, arrayList);
        listTablesView.setAdapter(adapter);
    }

    private void rawQuery() {
        db = new DB(this);
        db.open();
        Cursor c = DB.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(0).equals("android_metadata") || c.getString(0).equals("sqlite_sequence")) {
                    c.moveToNext();
                } else {
                    arrayList.add(c.getString(0));
                    c.moveToNext();
                }
            }
        }
        c.close();
        db.close();
    }
}