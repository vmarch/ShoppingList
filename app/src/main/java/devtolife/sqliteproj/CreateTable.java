package devtolife.sqliteproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateTable extends AppCompatActivity implements View.OnClickListener {

    private Button create;
    private Button cancel;
    private EditText newList;
    DB db;
    private String nameOfNewTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_table_layout);

        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        create = (Button) findViewById(R.id.btn_create_list);
        create.setOnClickListener(this);

        newList = (EditText) findViewById(R.id.add_new_table);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_create_list:
                nameOfNewTable = newList.getText().toString();
                if (!nameOfNewTable.isEmpty()) {

                    db = new DB(this);
                    db.open();
                    // TODO checking for isExist of table
                    // TODO checking for Integer name

                    DB.setNameOfTable(nameOfNewTable);

                    DB.database.execSQL(
                            "create table " + "\'" + nameOfNewTable + "\'" + "("
                                    + DB.KEY_ID + " integer primary key autoincrement,"
                                    + DB.KEY_NAME + " TEXT" + ")");

                    db.close();

                    Intent intent = new Intent(this, ProdActivity.class);
                    startActivity(intent);
                    finish();
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
}