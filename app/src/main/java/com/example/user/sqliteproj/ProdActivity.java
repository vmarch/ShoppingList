package com.example.user.sqliteproj;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ProdActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener, LoaderManager.LoaderCallbacks<Cursor> {

    DB db;
    DBHelper dbHelper;
    SimpleCursorAdapter scAdapter;
    Context context;
    String name;


    private View onTouchedItemView;

    ListView lv_list;
    Button btnEmpty, btnAdd;
    TextView tvNameId, tvNameName, tvNameCost;
    EditText tvName;

    private int pointDownX;
    private int pointDownY;
    private long touchedItemID;
    private int targetPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_layout);

        getSupportActionBar().setTitle("" + DB.getNameOfTable());

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.KEY_ID, DB.KEY_NAME, DB.KEY_COST};
        int[] to = new int[]{R.id.tv_list_id, R.id.tv_list_name, R.id.tv_list_cost};

        scAdapter = new SimpleCursorAdapter(this, R.layout.prod_item, null, from, to, 0);

        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setAdapter(scAdapter);

        lv_list.setOnTouchListener(this);
        getSupportLoaderManager().initLoader(0, null, this);

        tvNameId = (TextView) findViewById(R.id.tv_name_id);
        tvNameName = (TextView) findViewById(R.id.tv_name_name);

        tvNameCost = (TextView) findViewById(R.id.tv_name_cost);

        tvName = (EditText) findViewById(R.id.tv_name);
        tvName.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);


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


    @Override
    public void onClick(View view) {
        name = tvName.getText().toString();

        switch (view.getId()) {
            case R.id.btn_add:
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    db.addRec(name);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    tvName.setText("");
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int dispWidth = v.getWidth();
        int horizontalMinDistance = dispWidth / 4;
        int distFromRightBorder = dispWidth - pointDownX;
        int distToMeet = pointDownX / 10;


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                pointDownX = (int) event.getX();
                pointDownY = (int) event.getY();
                onTouchedItemView = lv_list.getChildAt(lv_list.pointToPosition(pointDownX, pointDownY));
                touchedItemID = lv_list.pointToRowId(pointDownX, pointDownY);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (onTouchedItemView != null) {
                    int pointMovingX = (int) event.getX();
                    int deltaX = pointDownX - pointMovingX;
                    int distGone = distFromRightBorder / distToMeet * Math.abs(deltaX);

                    if (deltaX < 0) {
//                  to right
                        if (Math.abs(deltaX) < distToMeet) {
                            targetPoint = dispWidth - pointMovingX + pointDownX - distGone;

                        } else {
                            targetPoint = pointMovingX;

//                        if (Math.abs(deltaX) > horizontalMinDistance && getDispWidth() - getPointMovingX() < getDispWidth() / 5) {
//
//                        TODO right swipe
////                        }
                        }
                        anim();

                    } else {

                        if (Math.abs(deltaX) < distToMeet) {
                            targetPoint = pointMovingX - dispWidth + distFromRightBorder - distGone;

                        } else {
                            targetPoint = pointMovingX - dispWidth;

                            if (Math.abs(deltaX) > horizontalMinDistance && pointMovingX < dispWidth / 5) {
                                onTouchedItemView.setBackgroundResource(R.color.deletemarker);

                            } else {
                                onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                            }
                        }
                        anim();
                    }
                    return true;
                } else {
                    lv_list.clearFocus();
                }


            case MotionEvent.ACTION_UP:
                if (onTouchedItemView != null) {
                    float upX = (int) event.getRawX();

                    if (upX < dispWidth / 5) {

                        db.delRec(touchedItemID);

                        getSupportLoaderManager().getLoader(0).forceLoad();
                        onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                        targetPoint = 0;
                        anim();
                    } else {

                        targetPoint = 0;
                        anim();
                    }
                    return true;
                } else {
                    lv_list.clearFocus();
                }

        }
        return true;
    }


    public void anim() {
        if (onTouchedItemView != null) {
            onTouchedItemView.animate()
                    .x(targetPoint)
                    .setDuration(0)
                    .start();
        } else {
            lv_list.clearFocus();
        }
    }

    private static class MyCursorLoader extends CursorLoader {
        DB db;

        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getAllData();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}


