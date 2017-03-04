package com.example.user.sqliteproj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnEmpty, btnRead, btnClear;
    TextView tvNameId, tvNameName, tvNamePrice, tvNameQuantity, tvNameKind, tvNameCost;
    EditText tvName, tvPrice, tvQuantity, tvKind;
    DBHelper dbHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> mDataset;
    Product product;

    Context context;


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

        mDataset = new ArrayList<>();


        mRecyclerView = (RecyclerView) findViewById(R.id.my_rec_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(context, mDataset);

        mRecyclerView.setAdapter(mAdapter);

        dbHelper = new DBHelper(this);

        fillProduct();
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

                    cv.put(DBHelper.KEY_NAME, name);
                    cv.put(DBHelper.KEY_PRICE, price);
                    cv.put(DBHelper.KEY_QUANTITY, quantity);
                    cv.put(DBHelper.KEY_KIND, kind);

                    database.insert(DBHelper.TABLE_LISTBUY, null, cv);
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
                fillProduct();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_LISTBUY, null, null);
                break;

        }
        dbHelper.close();

    }

    private void fillProduct() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_LISTBUY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int quantityIndex = cursor.getColumnIndex(DBHelper.KEY_QUANTITY);
            int kindIndex = cursor.getColumnIndex(DBHelper.KEY_KIND);
            int costIndex = cursor.getColumnIndex(DBHelper.KEY_COST);

            do {
                product = new Product(cursor.getInt(idIndex), cursor.getString(nameIndex),
                        cursor.getDouble(priceIndex),
                        cursor.getDouble(quantityIndex),
                        cursor.getInt(kindIndex),
                        cursor.getDouble(costIndex));
                               mDataset.add(product);
                mAdapter.notifyDataSetChanged();


                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", price = " + cursor.getDouble(priceIndex) +
                        ", quantity = " + cursor.getDouble(quantityIndex) +
                        ", kind = " + cursor.getInt(kindIndex) +
                        ", cost = " + cursor.getDouble(costIndex));
            } while (cursor.moveToNext());

        } else Log.d("mLog", "0 rows");

        cursor.close();
        dbHelper.close();

    }

}


