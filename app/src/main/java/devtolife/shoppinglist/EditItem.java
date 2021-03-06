package devtolife.shoppinglist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import devtolife.shoppinglist.data_base.DB;

public class EditItem extends AppCompatActivity implements View.OnClickListener {
    DB dbEd;
    private EditText editOldText;
    private Button btnOk, btnNo;
    private static String oldText;
    private static String newText;
    private SharedPreferences mSharedPref;

    public static String getOldText() {
        return oldText;
    }

    public static void setOldText(String oldText) {
        EditItem.oldText = oldText;
    }

    public static String getNewText() {
        return newText;
    }

    public static void setNewText(String newText) {
        EditItem.newText = newText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(mSharedPref.getInt("mytheme", 0));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item_layout);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_item);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Змінити завдання");
        } catch (Exception e) {
        }

        btnOk = findViewById(R.id.ok_new_text);
        btnOk.setOnClickListener(this);

        btnNo = findViewById(R.id.cancel_new_text);
        btnNo.setOnClickListener(this);

        editOldText = findViewById(R.id.edit_of_item);
        editOldText.setText(getOldText());

        editOldText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                setNewText(editOldText.getText().toString());
                if (!getNewText().isEmpty()) {

                    if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        changeTextOfItem();
                        return true;
                    } else if (actionId == EditorInfo.IME_ACTION_GO) {
                        changeTextOfItem();
                        return true;
                    } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                        changeTextOfItem();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ok_new_text:

                setNewText(editOldText.getText().toString());

                if (!getNewText().isEmpty()) {
                    changeTextOfItem();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Введіть нову назву завдання!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel_new_text:
                finish();
                break;
        }
    }

    private void changeTextOfItem() {
        // TODO checking for isExist of table
        dbEd = new DB(this);
        dbEd.open();
        dbEd.upDateName(ProdList.getItemIdInContextMenu(), getNewText());
        dbEd.close();
        finish();
    }
}
