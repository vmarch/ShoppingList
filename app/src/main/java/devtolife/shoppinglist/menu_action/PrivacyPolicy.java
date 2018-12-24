package devtolife.shoppinglist.menu_action;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import devtolife.shoppinglist.R;

public class PrivacyPolicy extends AppCompatActivity implements View.OnClickListener {
    private TextView privacy;

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

        privacy = findViewById(R.id.textViewPrivacyFull);
        privacy.setText(Html.fromHtml(getString(R.string.privacy_full_ukr)));
        privacy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.policy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_eng: {
                privacy.setText(Html.fromHtml(getString(R.string.privacy_full_eng)));
                break;
            }
            case R.id.menu_ukr: {
                privacy.setText(Html.fromHtml(getString(R.string.privacy_full_ukr)));
                break;
            }
            case R.id.menu_rus: {
                privacy.setText(Html.fromHtml(getString(R.string.privacy_full_rus)));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }
}

