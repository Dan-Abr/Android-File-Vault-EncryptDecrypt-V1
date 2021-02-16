package com.naq.encrydcryapp;
// Setting activity is for in application at the top corner, where the three dots are.
// By clicking those three dots, the user is able to select the algorithm type: AES or Blowfish

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    // variables initialization
    Button buttonSave;
    private RadioGroup radioGroup;
    private RadioButton radioButtonAES, radioButtonBLOWFISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Type casting
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonAES = (RadioButton) findViewById(R.id.radioButtonAES);
        radioButtonBLOWFISH = (RadioButton) findViewById(R.id.radioButtonBLOWFISH);

        setCurrentSettings();


        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
    }

    // choice to select the AES or Blowfish
    public void setCurrentSettings() {

        String currentAlgo = getDefaultAlgo(getApplicationContext());
        switch (currentAlgo) {

            case "AES Algorithm":
                radioButtonAES.setChecked(true);
                break;
            case "BLOWFISH Algorithm":
                radioButtonBLOWFISH.setChecked(true);
                break;
            default:
                break;
        }
    }

    // after selecting save the setting
    public void saveSettings() {

        String PREFES = "mysettings";
        RadioButton radioButtonTemp;
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(),"Please choose any of the Algorithm type", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int type = radioGroup.getCheckedRadioButtonId();
            radioButtonTemp = (RadioButton) findViewById(type);
            String value = radioButtonTemp.getText().toString();


            SharedPreferences preferences = getSharedPreferences(PREFES, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("type", value);
            editor.commit();

            Toast.makeText(Settings.this, value + " selected " + "Please decrypt and enrcypt file with the same encryption Type", Toast.LENGTH_LONG).show();

        }
    }


    public static String getDefaultAlgo(Context context) {
        String PREFES = "mysettings";
        SharedPreferences preferences = context.getSharedPreferences(PREFES, 0);
        if (preferences.getString("type", "empty").equalsIgnoreCase("empty")) {
            return "empty";
        } else {
            return preferences.getString("type", "");
        }
    }
}
