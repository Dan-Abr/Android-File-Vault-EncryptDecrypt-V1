package com.naq.encrydcryapp;
// this activity of code is for how encrypt action is perform
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class Encrypt extends AppCompatActivity {
    // variable initialization
    private static final int READ_REQUEST_CODE = 42;
    Button btnBrowse, btnEncrypt;
    String filepath;
    EditText etFilepath;
    String password;

    // ProgressDialog shows the progress of a task.
    private ProgressDialog progressDialog;
    // A Handler allows to send and process Message and Runnable objects associated with a thread's MessageQueue.
    // Each Handler instance is associated with a single thread and that thread's message queue
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
            Toast.makeText(getApplicationContext(), filename + " " + "encrypted", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        // Type Casting
        // We want to cast it as DrawerLayout because we may want to call methods on the instance that are defined in that class.
        // Since Java is a type-safe language, the compiler can be sure that an object is of a certain class, it won't let us call methods (or access fields) for that class.
        // start
        btnEncrypt = (Button) findViewById(R.id.button10);
        btnBrowse = (Button) findViewById(R.id.browse);
        etFilepath = (EditText) findViewById(R.id.etFilePath);
        final TextInputLayout textPasswordWrapper = (TextInputLayout) findViewById(R.id.etPasswordWrapperTime);
        textPasswordWrapper.setHint("Password");


        // Encrypt button
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = textPasswordWrapper.getEditText().getText().toString();

                if (password.isEmpty()) {
                    textPasswordWrapper.setError("Enter Password");
                    return;
                } else {
                    textPasswordWrapper.setError("");
                }

                if (filepath == null) {
                    Toast.makeText(getApplicationContext(), "Select File", Toast.LENGTH_SHORT).show();
                    return;
                }

                // filepath
                String vaultPath = Environment.getExternalStorageDirectory().getPath()+"/Vault/";
                String decryptedPath = Environment.getExternalStorageDirectory().getPath()+"/decrypted/";

                String encryptionType = Settings.getDefaultAlgo(getApplicationContext());
                final Encryption encryption = new Encryption(encryptionType, vaultPath, decryptedPath);

                final String pasHash = password + "#";
                progressDialog = ProgressDialog.show(Encrypt.this, "", "Encrypting");

                encrypt(encryption, pasHash);
            }
        });


        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This intent is used to browse files from storage when the user clicks on the browse button and media is shown
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

        if (ContextCompat.checkSelfPermission(Encrypt.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(Encrypt.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(Encrypt.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);


            }
        } else {

        }


    }
    // Thread process to speedup the decrypting speed when possible.
    // Also try-catch methods to check any failures
    private void encrypt(final Encryption encryption, final String pasHash) {

        new Thread() {
            public void run() {

                try {
                    encryption.encrypt(encryption.hashGenerator(pasHash), filepath);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NoSuchPaddingException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (InvalidKeyException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (InvalidAlgorithmParameterException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // code for encrypting file store
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                FileManager fileManager = new FileManager();
                String path = fileManager.getRealPathFromURI(getApplicationContext(), uri);
                filepath = path;
                String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
                etFilepath.setText(filename);

            }
        }
    }



}