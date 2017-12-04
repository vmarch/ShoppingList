package devtolife.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import devtolife.shoppinglist.data_base.DB;
import devtolife.shoppinglist.menu_action.PrivacyPolicy;
import devtolife.shoppinglist.menu_action.SettingAboutAppActivity;
import devtolife.shoppinglist.menu_action.ThemeSettings;

public class MainList extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private DB dbML;
    private ListView listTablesView;
    private List<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private View onTouchedItemView;
    private SharedPreferences mSharedPref;
    private String MYTHEME = "mytheme";
    private Intent intent;
    private int numItemOfList = 0;
    private String nameItemOfList = "name";
    private String itemOfList;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt(MYTHEME, 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer);


        toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(toggle);



// Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
//TODO change "R.mipmap.ic_launcher"
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        if (id == R.id.action_theme) {
                            intent = new Intent(MainList.this, ThemeSettings.class);
                            startActivity(intent);
                            finish();

                        } else if (id == R.id.action_rate) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse
                                    ("https://play.google.com/store/apps/details?id=devtolife.shoppinglist"));
                            startActivity(intent);

                        } else if (id == R.id.action_policy) {
                            intent = new Intent(MainList.this, PrivacyPolicy.class);
                            startActivity(intent);

                        } else if (id == R.id.action_about_app) {
                            intent = new Intent(MainList.this, SettingAboutAppActivity.class);
                            startActivity(intent);
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainList.this, CreateTable.class);
                startActivity(intent);
            }
        });


        listTablesView = findViewById(R.id.list_table);

        registerForContextMenu(listTablesView);

        rawQuery();

        adapter = new ArrayAdapter<>(this, R.layout.main_item, arrayList);
        listTablesView.setAdapter(adapter);
        listTablesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onTouchedItemView = v;
                TextView tv = (TextView) onTouchedItemView;
                DB.setNameOfTable(tv.getText().toString());
                intent = new Intent(MainList.this, ProdList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == android.R.id.home) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
                dbML = new DB(this);
                dbML.open();
                dbML.deleteTable(DB.getNameOfTable());
                dbML.close();
                updateAdapter();
                return true;
            case R.id.share_list:
                dbML = new DB(this);
                dbML.open();
                tableOfList();
                shareOfList();
                dbML.close();
                return true;

//            case R.id.copy_list:
//                TODO Copy of LIST with changing of Name
//                dbML = new DB(this);
//                dbML.open();
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
//                            dbML.close();
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
//                        dbML.addRec(cList.getString(1),0,0.0,1,1);
//                        cList.moveToNext();
//                    }
//                }
//                cList.close();
//                dbML.close();
//
//                Intent intent = new Intent(this, ProdList.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void shareOfList() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "" + DB.getNameOfTable() + "\n" + itemOfList);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void tableOfList() {
        int i = 1;
        itemOfList = "";

        Cursor c = dbML.getAllItemForShare();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(0) == null && c.getString(0).isEmpty()) {
                    c.moveToNext();
                } else {
                    numItemOfList = i;
                    nameItemOfList = c.getString(1);
                    itemOfList = itemOfList + "\n" + numItemOfList + ". " + nameItemOfList + ";";
                    c.moveToNext();
                    i++;
                }
            }
        }
        c.close();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAdapter();
    }

    private void updateAdapter() {
        adapter.clear();
        rawQuery();
        adapter = new ArrayAdapter<>(this, R.layout.main_item, arrayList);
        listTablesView.setAdapter(adapter);
    }

    private void rawQuery() {
        dbML = new DB(this);
        dbML.open();
        Cursor c = DB.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(0).equals("android_metadata")
                        || c.getString(0).equals("sqlite_sequence")
                        || c.getString(0).equals("Example")) {
                    c.moveToNext();
                } else {
                    arrayList.add(c.getString(0));
                    c.moveToNext();
                }
            }
        }
        c.close();
        dbML.close();
    }

}