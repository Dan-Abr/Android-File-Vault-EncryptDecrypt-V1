package com.naq.encrydcryapp;
// this activity of code is for user either want to see encrypted file list or decrypted file list
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Encrypt_Decrypt extends AppCompatActivity {
    // variable initialization
    TextView Encrypted;
    TextView Decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_dcrypt);
        // Type Casting
        // We want to cast it as DrawerLayout because we may want to call methods on the instance that are defined in that class.
        // Since Java is a type-safe language, the compiler can be sure that an object is of a certain class, it won't let us call methods (or access fields) for that class.
        // start
        Encrypted = (TextView) findViewById(R.id.Encryptedfile);
        Encrypted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEncrypted();

            }

            private void chooseEncrypted() {
                // This intent is used to jump from one activity to another activity
                Intent i = new Intent(Encrypt_Decrypt.this, FilesActivity.class);
                startActivity(i);
            }
        });

        Decrypt = (TextView) findViewById(R.id.Decryptedfiles);

        Decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDecrypted();

            }

            private void chooseDecrypted() {
                Intent j = new Intent(Encrypt_Decrypt.this, Decrypted.class);
                startActivity(j);
            }
        });








    }
}