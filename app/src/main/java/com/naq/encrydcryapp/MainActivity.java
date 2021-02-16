package com.naq.encrydcryapp;

// this is the first main activity from where the application flow start

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //type casting
        TextView buttonEncrypt = (TextView) findViewById(R.id.file);
        buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEncrypt();

            }
        });

        TextView buttonVault = (TextView) findViewById(R.id.vault);
        buttonVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseVault();
            }
        });


        createFolders();
        setDefaultSettings();

        // permission handler Read/write
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, 1024);

            }
        } else {}
    }

    // intent to jump from one activity to another
    private void chooseEncrypt() {
        Intent i = new Intent(MainActivity.this, Encrypt.class);
        startActivity(i);
    }
    // intent to jump from one activity to another
    private void chooseVault() {
        Intent i = new Intent(MainActivity.this, Encrypt_Decrypt.class);
        startActivity(i);
    }

    // this code is to create a folder in the local mobile device
    public void createFolders() {

        File dirVault = new File(Environment.getExternalStorageDirectory().getPath() + "/Vault");
        File dirDecrypted = new File(Environment.getExternalStorageDirectory().getPath() + "/Decrypted");

        try {

            if (!dirVault.exists()) {
                if (dirVault.mkdir()) {
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot create folders, please Manually create Folder named Vault and Decrypted in your internal storage", Toast.LENGTH_SHORT).show();
                }
            }

            if (!dirDecrypted.exists()) {
                if (dirDecrypted.mkdir()) {
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot create folders, please Manually create Folder named Vault and Decrypted in your internal storage", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this is for choose the encryption type: AES OR BLOWFISH
    public void setDefaultSettings() {
        String algoType = Settings.getDefaultAlgo(getApplicationContext());
        if (algoType.equalsIgnoreCase("empty")) {
            String PREFES = "mysettings";
            // Android provides many ways of storing data of an application. One of this way is called Shared Preferences.
            // Shared Preferences allow you to save and retrieve data in the form of key,value pair.
            SharedPreferences preferences = getSharedPreferences(PREFES, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("type", "AES");
            editor.commit();
        }
    }

    // when we press the back button to logout from the application
    @Override
    public void onBackPressed() {
        signOut();
    }

    private void signOut() {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
        a_builder.setTitle("Confirm Exit?");
        a_builder.setMessage("Log out from application?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();

        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
