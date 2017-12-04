package devtolife.shoppinglist.menu_action;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import devtolife.shoppinglist.MainList;
import devtolife.shoppinglist.R;

public class ThemeSettings extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences mSharedPref;
    private Button btnPink, btnYellow, btnGreen, btnBlue, btnGrey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_theme_layout);

        btnPink = findViewById(R.id.btn_pink);
        btnYellow = findViewById(R.id.btn_yellow);
        btnGreen = findViewById(R.id.btn_green);
        btnBlue = findViewById(R.id.btn_blue);
        btnGrey = findViewById(R.id.btn_grey);

        btnPink.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnGrey.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar_setting_themes);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ThemeSettings.this, MainList.class);
                startActivity(intent1);
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Теми");
        } catch (Exception e) {
        }

    }


    @Override
    public void onClick(View v) {

        Intent intent2;
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = mSharedPref.edit();

        switch (v.getId()) {

            case R.id.btn_green:
                ed.putInt("mytheme", R.style.AppThemeGreen);
                ed.apply();
                intent2 = getIntent();
                startActivity(intent2);
                finish();

                break;
            case R.id.btn_yellow:
                ed.putInt("mytheme", R.style.AppThemeYellow);
                ed.apply();
                intent2 = getIntent();
                startActivity(intent2);
                finish();
                break;

            case R.id.btn_blue:
                ed.putInt("mytheme", R.style.AppThemeBlue);
                ed.apply();
                intent2 = getIntent();
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_grey:
                ed.putInt("mytheme", R.style.AppThemeGrey);
                ed.apply();
                intent2 = getIntent();
                startActivity(intent2);
                finish();
                break;

            case R.id.btn_pink:
                ed.putInt("mytheme", R.style.AppThemePink);
                ed.apply();
                intent2 = getIntent();
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(this, MainList.class);
        startActivity(intent1);
        finish();

    }
}
