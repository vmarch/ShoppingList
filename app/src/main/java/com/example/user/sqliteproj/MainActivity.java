package com.example.user.sqliteproj;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "myLogs";
    Button btnAdd, btnEmpty, btnRead, btnClear;
    TextView tvNameId, tvNameName, tvNamePrice, tvNameQuantity, tvNameKind, tvNameCost,
            tvRecId, tvRecName, tvRecPrice, tvRecQuantity, tvRecKind, tvRecCost;
    EditText tvName, tvPrice, tvQuantity, tvKind;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNameId = (TextView) findViewById(R.id.tvNameId);
        tvNameName = (TextView) findViewById(R.id.tvNameName);
        tvNamePrice = (TextView) findViewById(R.id.tvNamePrice);
        tvNameQuantity = (TextView) findViewById(R.id.tvNameQuantity);
        tvNameKind = (TextView) findViewById(R.id.tvNameKind);
        tvNameCost = (TextView) findViewById(R.id.tvNameCost);

        tvRecId = (TextView) findViewById(R.id.tvRecId);
        tvRecName = (TextView) findViewById(R.id.tvRecName);
        tvRecPrice = (TextView) findViewById(R.id.tvRecPrice);
        tvRecQuantity = (TextView) findViewById(R.id.tvRecQuantity);
        tvRecKind = (TextView) findViewById(R.id.tvRecKind);
        tvRecCost = (TextView) findViewById(R.id.tvRecCost);

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

        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

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

                    cv.put("name", name);
                    cv.put("price", price);
                    cv.put("quantity", quantity);
                    cv.put("kind", kind);
                    cv.put("cost", 0.0);
                    database.insert("listbuy", null, cv);
                }
                break;

            case R.id.btnEmpty:
                tvName.setText("");
                tvPrice.setText("");
                tvQuantity.setText("");
                tvKind.setText("");
                break;

            case R.id.btnRead:

                Cursor c = database.query("listbuy", null, null, null, null, null, null);

                if (c.moveToFirst()) {

                    int idColIndex = c.getColumnIndex("_id");
                    int nameColIndex = c.getColumnIndex("name");
                    int priceColIndex = c.getColumnIndex("price");
                    int quantityColIndex = c.getColumnIndex("quantity");
                    int kindColIndex = c.getColumnIndex("kind");
                    int costColIndex = c.getColumnIndex("cost");

                    do {

                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", price = " + c.getDouble(priceColIndex) +
                                        ", quantity = " + c.getDouble(quantityColIndex) +
                                        ", kind = " + c.getInt(kindColIndex) +
                                        ", cost = " + c.getDouble(costColIndex));

                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();

                break;

            case R.id.btnClear:
                database.delete("listbuy", null, null);
                break;

        }
    }
}
