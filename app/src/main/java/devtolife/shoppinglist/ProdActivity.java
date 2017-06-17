package devtolife.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    Button btnAdd;
    TextView tvNameId, tvNameName;
    EditText tvName;

    private int pointDownX;
    private int pointDownY;
    private long touchedItemID;
    private int targetPoint;
    private boolean moved = false;
    private boolean justscroll;
    private int pointMovingX;
    private int pointMovingY;
    SharedPreferences mSharedPref;

    public boolean isJustscroll() {
        return justscroll;
    }

    public void setJustscroll(boolean justscroll) {
        this.justscroll = justscroll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

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
                if (lv_list.getCount() > 0) {
                    setJustscroll(true);
                    pointDownX = (int) event.getX();
                    pointDownY = (int) event.getY();
                    touchedItemID = lv_list.pointToRowId(pointDownX, pointDownY);
                    onTouchedItemView = getViewByPosition(lv_list.pointToPosition(pointDownX, pointDownY), lv_list);
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Let's start new list!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            case MotionEvent.ACTION_MOVE:

                if (onTouchedItemView != null) {
                    moved = false;
                    if (isJustscroll()) {

                        pointMovingX = (int) event.getX();
                        pointMovingY = (int) event.getY();
                        int deltaX = pointDownX - pointMovingX;
                        int deltaY = pointDownY - pointMovingY;
                        if (deltaX < 0 && Math.abs(deltaY) < 30) {
//                       to right
//TODO use right swipe
//                            targetPoint = pointMovingX - pointDownX;
//                            if (Math.abs(deltaX) > horizontalMinDistance && dispWidth - pointMovingX < dispWidth / 5) {
//                                onTouchedItemView.setBackgroundResource(R.color.colorPrimaryDark);
//                            } else {
//                                onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
//                            }
//                            anim();
                        } else if (deltaX > 0 && Math.abs(deltaY) < 30) {
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

                        } else if (deltaX == 0 && Math.abs(deltaY) < 20) {
                        } else {

                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                            targetPoint = 0;
                            anim();
                            setJustscroll(false);

                        }
                    }
                }
                return false;

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
        return true;
    }

    private View getViewByPosition(int pos, ListView lv) {
        final int firstListItemPosition = lv.getFirstVisiblePosition();

        final int childIndex = pos - firstListItemPosition;
        return lv.getChildAt(childIndex);
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
        db.close();
        super.onDestroy();
    }
}


