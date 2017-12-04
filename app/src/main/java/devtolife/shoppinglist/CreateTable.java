package devtolife.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import devtolife.shoppinglist.data_base.DB;

public class CreateTable extends AppCompatActivity implements View.OnClickListener {

    DB dbCT;
    SharedPreferences mSharedPref;
    private Button create;
    private Button cancel;
    private EditText newList;
    private String nameOfNewTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table_layout);

        Toolbar toolbar = findViewById(R.id.toolbar_create_table);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Створити список");
        } catch (Exception e) {
        }

        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        create = findViewById(R.id.btn_create_list);
        create.setOnClickListener(this);

        newList = findViewById(R.id.add_new_table);
        newList.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                nameOfNewTable = newList.getText().toString();
                if (!nameOfNewTable.isEmpty()) {

                    if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        createNewList();
                        return true;
                    } else if (actionId == EditorInfo.IME_ACTION_GO) {
                        createNewList();
                        return true;
                    } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                        createNewList();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_create_list:
                nameOfNewTable = newList.getText().toString();

                if (nameOfNewTable.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Введіть назву списку!", Toast.LENGTH_SHORT).show();

                } else if (!nameOfNewTable.isEmpty() && Pattern.matches("(\\s)*", nameOfNewTable + "")) {

                    Toast.makeText(getApplicationContext(),
                            "Пуста назва. \nВведіть знову!", Toast.LENGTH_SHORT).show();
                    newList.setText("");

                } else {

                    createNewList();
                }
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void createNewList() {
        dbCT = new DB(this);
        dbCT.open();
        // TODO checking for isExist of table

        DB.setNameOfTable(nameOfNewTable);

        DB.database.execSQL(
                "create table " + "\'" + nameOfNewTable + "\'" + "("
                        + DB.KEY_ID + " integer primary key autoincrement,"
                        + DB.KEY_NAME + " TEXT,"
                        + DB.KEY_CHECKED + " integer,"
                        + DB.KEY_PRICE + " double,"
                        + DB.KEY_QUANTITY + " integer,"
                        + DB.KEY_IMPORTANT + " integer"
                        + ")");
        dbCT.close();

        Intent intent = new Intent(this, ProdList.class);
        startActivity(intent);
        finish();
    }
}