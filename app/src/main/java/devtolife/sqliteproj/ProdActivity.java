package devtolife.sqliteproj;

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
    private boolean moved = false;
    private int horizontalMinDistance;


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
        horizontalMinDistance = dispWidth / 3;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                pointDownX = (int) event.getX();
                pointDownY = (int) event.getY();
                onTouchedItemView = lv_list.getChildAt(lv_list.pointToPosition(pointDownX, pointDownY));
                touchedItemID = lv_list.pointToRowId(pointDownX, pointDownY);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (onTouchedItemView != null) {
                    moved = false;
                    int pointMovingX = (int) event.getX();
                    int pointMovingY = (int) event.getY();
                    int deltaX = pointDownX - pointMovingX;
                    int deltaY = pointDownY - pointMovingY;

                    if (deltaX < 0 && Math.abs(deltaY) < 30 && Math.abs(deltaX) > Math.abs(deltaY)) {
//                       to right
                        targetPoint = pointMovingX - pointDownX;
                        if (Math.abs(deltaX) > horizontalMinDistance && dispWidth - pointMovingX < dispWidth / 5) {
                            onTouchedItemView.setBackgroundResource(R.color.colorPrimaryDark);

                        } else {
                            onTouchedItemView.setBackgroundResource(R.color.colorOfItem);

                        }
                        anim();
//                        return true;

                    } else if (deltaX > 0 && Math.abs(deltaY) < 30 && Math.abs(deltaX) > Math.abs(deltaY)) {
//                        to left
                        targetPoint = pointMovingX - pointDownX;
                        if (Math.abs(deltaX) > horizontalMinDistance && pointMovingX < dispWidth / 5) {
                            onTouchedItemView.setBackgroundResource(R.color.deletemarker);
                            moved = true;
                        } else {
                            onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                            moved = false;
                        }
                        anim();
//
//                        return true;
                    } else {
                        onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                        targetPoint = 0;
                        anim();
                        lv_list.clearFocus();
                    }
                }
                return true;


            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onTouchedItemView != null) {
                    if (moved) {
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


