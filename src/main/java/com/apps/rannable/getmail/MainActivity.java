package com.apps.rannable.getmail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    EditText mailET;
    Button copyBtn;
    SharedPreferences sharedPreferences;
    CheckBox checkBox;
    String currentEmail;

    private static final String TAG = "MainActivity";
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable the action bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mailET = (EditText)findViewById(R.id.editText);
        copyBtn = (Button)findViewById(R.id.button);
        checkBox = (CheckBox)findViewById(R.id.checkBox);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        currentEmail = sharedPreferences.getString("crntemail", "");
        mailET.setText(currentEmail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);




        if(sharedPreferences.getBoolean("checked", true)){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myEmail = mailET.getText().toString();


                if(checkBox.isChecked()){
                    if(isEmailValid(myEmail)){
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("my email", myEmail);
                        clipboard.setPrimaryClip(clip);

                        Toasty.success(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{

                        mailET.setError("Invalid email address");
                    }
                }else{
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("my email", myEmail);
                    clipboard.setPrimaryClip(clip);
                    Toasty.success(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();

                    finish();

                }











            }
        });


    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(checkBox.isChecked()){
            sharedPreferences.edit().putBoolean("checked", true).commit();
        }else{
            sharedPreferences.edit().putBoolean("checked", false).commit();

        }

        currentEmail = mailET.getText().toString();
        sharedPreferences.edit().putString("crntemail", currentEmail).commit();

    }
}
