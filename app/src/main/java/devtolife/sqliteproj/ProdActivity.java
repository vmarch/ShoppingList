package devtolife.sqliteproj;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ProdActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DEBUG_TAG = "Gecture";
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
    private boolean moved = false;
    private boolean justscroll;

    public boolean isJustscroll() {
        return justscroll;
    }

    public void setJustscroll(boolean justscroll) {
        this.justscroll = justscroll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_layout);

        getSupportActionBar().setTitle("" + DB.getNameOfTable());

        db = new DB(this);
        db.open();

        String[] from = new String[]{DB.KEY_ID, DB.KEY_NAME};
        int[] to = new int[]{R.id.tv_list_id, R.id.tv_list_name};

        scAdapter = new SimpleCursorAdapter(this, R.layout.prod_item, null, from, to, 0);

        lv_list = (ListView) findViewById(R.id.lv_list);
        lv_list.setAdapter(scAdapter);

        lv_list.setOnTouchListener(this);
        getSupportLoaderManager().initLoader(0, null, this);

        tvNameId = (TextView) findViewById(R.id.tv_name_id);
        tvNameName = (TextView) findViewById(R.id.tv_name_name);

        tvName = (EditText) findViewById(R.id.tv_name);

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
        int horizontalMinDistance = dispWidth / 3;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setJustscroll(true);
                pointDownX = (int) event.getX();
                pointDownY = (int) event.getY();

//                onTouchedItemView = lv_list.getChildAt(lv_list.pointToPosition(pointDownX, pointDownY));

                onTouchedItemView = lv_list.getChildAt(lv_list.pointToPosition(pointDownX, pointDownY));
                touchedItemID = lv_list.pointToRowId(pointDownX, pointDownY);
                Log.d(DEBUG_TAG, "onDown: "
                        + touchedItemID + ", "
                        + onTouchedItemView + ",  "
                        + pointDownX + ",  "
                        + pointDownY + ", "
                        + isJustscroll());
                return true;

            case MotionEvent.ACTION_MOVE:
                if (onTouchedItemView != null) {
                    moved = false;

                    Log.d(DEBUG_TAG, "onMove1: "
                            + touchedItemID + ", "
                            + onTouchedItemView + ",  "
                            + pointDownX + ",  "
                            + pointDownY + ", "
                            + isJustscroll());

                    if (isJustscroll()) {

                        int pointMovingX = (int) event.getX();
                        int pointMovingY = (int) event.getY();
                        int deltaX = pointDownX - pointMovingX;
                        int deltaY = pointDownY - pointMovingY;
                        Log.d(DEBUG_TAG, "onMove2: "
                                + touchedItemID + ", "
                                + onTouchedItemView + ",  "
                                + pointDownX + ",  "
                                + pointDownY + ", deltaY: "
                                + deltaY + ", "
                                + isJustscroll());
                        if (deltaX <= 0 && Math.abs(deltaY) < 20
//                                && Math.abs(deltaX) > Math.abs(deltaY)
                                ) {
//                       to right
                            targetPoint = pointMovingX - pointDownX;
                            if (Math.abs(deltaX) > horizontalMinDistance && dispWidth - pointMovingX < dispWidth / 5) {
                                onTouchedItemView.setBackgroundResource(R.color.colorPrimaryDark);

                            } else {
                                onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                            }
                            anim();
                            return false;

                        } else if (deltaX > 0 && Math.abs(deltaY) < 20
//                                && Math.abs(deltaX) > Math.abs(deltaY)
                                ) {
//                        to left
                            Log.d(DEBUG_TAG, "onMoveToL: "
                                    + touchedItemID + ", "
//                                    + onTouchedItemView + ",  "
                                    + pointDownX + ",  "
                                    + pointDownY + ", "
                                    + lv_list.pointToPosition(pointDownX, pointDownY) + ", "
                                    + isJustscroll());
                            targetPoint = pointMovingX - pointDownX;
                            if (Math.abs(deltaX) > horizontalMinDistance && pointMovingX < dispWidth / 5) {
                                onTouchedItemView.setBackgroundResource(R.color.deletemarker);
                                moved = true;
                            } else {
                                onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                                moved = false;
                            }
                            anim();
                            return false;
                        } else {
                            Log.d(DEBUG_TAG, "onScroll: "
                                    + touchedItemID + ", "
//                                    + onTouchedItemView + ",  "
                                    + pointDownX + ",  "
                                    + pointDownY + ", "
                                    + lv_list.pointToPosition(pointDownX, pointDownY) + ", "
                                    + isJustscroll());
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                            targetPoint = 0;
                            anim();
                            lv_list.clearFocus();
                            setJustscroll(false);

                        }
                    }
                }
                return false;


            case MotionEvent.ACTION_UP:
                Log.d(DEBUG_TAG, "onUp: "
                        + touchedItemID + ", "
//                        + onTouchedItemView + ",  "
                        + pointDownX + ",  "
                        + pointDownY + ", "
                        + lv_list.pointToPosition(pointDownX, pointDownY) + ", "
                        + isJustscroll());
            case MotionEvent.ACTION_CANCEL:

                if (onTouchedItemView != null) {
                    if (moved) {
                        Log.d(DEBUG_TAG, "onDel: "
                                + touchedItemID + ", "
//                                + onTouchedItemView + ",  "
                                + pointDownX + ",  "
                                + pointDownY + ", "
                                + lv_list.pointToPosition(pointDownX, pointDownY) + ", "
                                + isJustscroll());
                        db.delRec(touchedItemID);
                        getSupportLoaderManager().getLoader(0).forceLoad();
                    }

                    onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                    targetPoint = 0;
                    anim();
                    moved = false;
                }
        }

        return false;
    }


    public void anim() {
        Log.d(DEBUG_TAG, "onAnim: "
                + touchedItemID + ", "
//                + onTouchedItemView + ",  "
                + pointDownX + ",  "
                + pointDownY + ", "
                + lv_list.pointToPosition(pointDownX, pointDownY) + ", "
                + isJustscroll());
        if (onTouchedItemView != null) {
            onTouchedItemView.animate()
                    .x(targetPoint)
                    .setDuration(0)
                    .start();
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


