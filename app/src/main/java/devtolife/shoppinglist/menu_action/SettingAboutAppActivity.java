package devtolife.shoppinglist.menu_action;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import devtolife.shoppinglist.BuildConfig;
import devtolife.shoppinglist.R;

public class SettingAboutAppActivity extends AppCompatActivity {
    SharedPreferences mSharedPref;
    TextView  tv_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_about_app);

        Toolbar toolbar = findViewById(R.id.toolbar_setting_about);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Про програму");
        } catch (Exception e) {
        }

        String versionName = BuildConfig.VERSION_NAME;

        tv_version = findViewById(R.id.version_of_app);
        tv_version.setText("Version of APP: " + versionName);



    }
}
