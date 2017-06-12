package devtolife.sqliteproj;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListTables extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private DB db;
    private ListView listTablesView;
    private Button btn;
    private List<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private View onTouchedItemView;
    private int pointDownX;
    private int pointDownY;
    private long touchedItemID;
    private int targetPoint;
    private View itemTouchedOnTouch;
    private int dispWidth;
    private int horizontalMinDistance;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private boolean moved = false;
    private boolean justscroll = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        btn = (Button) findViewById(R.id.btn_create);
        listTablesView = (ListView) findViewById(R.id.list_table);
        rawQuery();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, arrayList);
        listTablesView.setAdapter(adapter);

        listTablesView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean fact = false;
                itemTouchedOnTouch = v;

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        Log.d(DEBUG_TAG, "onDown1: " + event.toString());
                        pointDownX = (int) event.getX();
                        pointDownY = (int) event.getY();
                        onTouchedItemView = listTablesView.getChildAt(listTablesView.pointToPosition(pointDownX, pointDownY));
                        touchedItemID = listTablesView.pointToRowId(pointDownX, pointDownY);
                        if (onTouchedItemView != null) {
                            justscroll = true;
                            fact = mDetector.onTouchEvent(event);
                        } else {
                            fact = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (justscroll) {
                            if (onTouchedItemView != null) {
                                fact = mDetector.onTouchEvent(event);
                            } else {
                                fact = false;
                            }
                        }
                        break;


                    case MotionEvent.ACTION_UP:
                        if (onTouchedItemView != null) {
                            fact = mDetector.onTouchEvent(event);
                            onUp();
                        } else {
                            fact = false;
                        }
                        break;
                }

                return fact;
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() != MotionEvent.ACTION_MOVE) {
                    itemTouchedOnTouch = v;
                    return mDetector.onTouchEvent(event);
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDown2: " + event.toString());

        if (itemTouchedOnTouch.getId() == R.id.list_table) {
            Log.d(DEBUG_TAG, "onDown_List: /n" + event.toString());

            if (listTablesView.getCount() > 0) {
                pointDownX = (int) event.getX();
                pointDownY = (int) event.getY();
                onTouchedItemView = listTablesView.getChildAt(listTablesView.pointToPosition(pointDownX, pointDownY));
                touchedItemID = listTablesView.pointToRowId(pointDownX, pointDownY);
                dispWidth = listTablesView.getWidth();
                horizontalMinDistance = dispWidth / 3;

            } else {
                Toast.makeText(getApplicationContext(),
                        "Let's start new list!", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else //            Log.d(DEBUG_TAG, "onDown_BTN: " + event.toString());
            return itemTouchedOnTouch.getId() == R.id.btn_create;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());

        if (onTouchedItemView != null) {
            Log.d(DEBUG_TAG, "onScroll_LIST_ELEM: " + e1.toString() + e2.toString());

            int pointMovingX = (int) e2.getX();
            int pointMovingY = (int) e2.getY();
            int deltaX = pointDownX - pointMovingX;
            int deltaY = pointDownY - pointMovingY;

            if (deltaX < 0 && Math.abs(deltaY) < 30 && Math.abs(deltaX) > Math.abs(deltaY)) {
                Log.d(DEBUG_TAG, "onScroll_LIST_ELEM_to_RIGHT: " + e1.toString() + e2.toString());
//to right
//
//                targetPoint = pointMovingX - pointDownX;
//                if (Math.abs(deltaX) > horizontalMinDistance && dispWidth - pointMovingX < dispWidth / 5) {
//                    onTouchedItemView.setBackgroundResource(R.color.colorPrimaryDark);
//                } else {
//                    onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
//                }
//                anim();

            } else if (deltaX > 0 && Math.abs(deltaY) < 30 && Math.abs(deltaX) > Math.abs(deltaY)) {
//to left
                Log.d(DEBUG_TAG, "onScroll_LIST_ELEM_to_LEFT: " + e1.toString() + e2.toString());
                targetPoint = pointMovingX - pointDownX;
                if (Math.abs(deltaX) > horizontalMinDistance && pointMovingX < dispWidth / 5) {
                    onTouchedItemView.setBackgroundResource(R.color.deletemarker);
                    moved = true;

                } else {
                    onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                    moved = false;
                }
                anim();

            } else {
                justscroll = false;
                moved = false;
                onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
                targetPoint = 0;
                anim();
            }
        }
        return true;
    }

    public void onUp() {
        Log.d(DEBUG_TAG, "onUP " + moved);
        if (onTouchedItemView != null) {
            if (moved) {

                db = new DB(this);
                db.open();
                db.deleteTable(listTablesView.getAdapter().getItem((int) touchedItemID).toString());
                db.close();
                updateAdapter();

            }
            onTouchedItemView.setBackgroundResource(R.color.colorOfItem);
            targetPoint = 0;
            anim();
            moved = false;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());

        Intent intent;
        switch (itemTouchedOnTouch.getId()) {

            case R.id.list_table:
                if (onTouchedItemView != null) {
//                    Log.d(DEBUG_TAG, "onSingleTapConfirmed_LIST: " + event.toString());
                    TextView tv = (TextView) onTouchedItemView;
                    DB.setNameOfTable(tv.getText().toString());

                    intent = new Intent(this, ProdActivity.class);
                    startActivity(intent);
                    return true;

                } else {
//                    Log.d(DEBUG_TAG, "onSingleTapConfirmed_LIST_NULL:  " + event.toString());
                    return false;
                }

            case R.id.btn_create:
//                Log.d(DEBUG_TAG, "onSingleTapConfirmed_BTN: " + event.toString());
                intent = new Intent(this, CreateTable.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    public void anim() {
        onTouchedItemView.animate()
                .x(targetPoint)
                .setDuration(0)
                .start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
//        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());

        if (itemTouchedOnTouch.getId() == R.id.btn_create) {
//                Log.d(DEBUG_TAG, "onLongPress_BTN: " + event.toString());
            Intent intent = new Intent(this, CreateTable.class);
            startActivity(intent);
        }
    }

    @Override
    public void onShowPress(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
//        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAdapter();
    }

    private void updateAdapter() {
        adapter.clear();
        rawQuery();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, arrayList);
        listTablesView.setAdapter(adapter);
    }

    private void rawQuery() {
        db = new DB(this);
        db.open();
        Cursor c = DB.database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                if (c.getString(0).equals("android_metadata") || c.getString(0).equals("sqlite_sequence")) {
                    c.moveToNext();
                } else {
                    arrayList.add(c.getString(0));
                    c.moveToNext();
                }
            }
        }
        c.close();
        db.close();
    }
}