package devtolife.shoppinglist;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreateTable extends AppCompatActivity implements View.OnClickListener {

    private Button create;
    private Button cancel;
    private EditText newList;
    DB db;
    private String nameOfNewTable;
    SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table_layout);


        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        create = (Button) findViewById(R.id.btn_create_list);
        create.setOnClickListener(this);

        newList = (EditText) findViewById(R.id.add_new_table);
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
                if (!nameOfNewTable.isEmpty()) {

                    createNewList();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter the name of new table", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void createNewList() {
        db = new DB(this);
        db.open();
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
        db.close();

        Intent intent = new Intent(this, ProdActivity.class);
        startActivity(intent);
        finish();
    }
}