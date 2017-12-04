package devtolife.shoppinglist.menu_action;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import devtolife.shoppinglist.R;


public class PrivacyPolicy extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy);

        Toolbar toolbar = findViewById(R.id.toolbar_setting_policy);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Політика конфіденційності");
        } catch (Exception e) {
        }

        TextView privacyText = findViewById(R.id.textViewPrivacy);

        Button btn_open = findViewById(R.id.btn_privacy);
        btn_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://vmarch.github.io/privacy_policy.html"));
        startActivity(intent);
    }
}
