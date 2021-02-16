package com.naq.encrydcryapp;
// this activity of code is for how decrypt action is perform
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;


public class Decrypt extends AppCompatActivity {
    // ProgressDialog shows the progress of a task.
    private ProgressDialog progressDialog;

    // A Handler allows to send and process Message and Runnable objects associated with a thread's MessageQueue.
    // Each Handler instance is associated with a single thread and that thread's message queue
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Decrypted", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        // final keyword = it is basically a constant. In this case, as it is an object, its reference cannot be changed.
        final String filename = getIntent().getStringExtra("filename");

        // Type Casting
        // We want to cast it as DrawerLayout because we may want to call methods on the instance that are defined in that class.
        // Since Java is a type-safe language, the compiler can be sure that an object is of a certain class, it won't let us call methods (or access fields) for that class.
        // start
        TextView tvFileName = (TextView) findViewById(R.id.tvFileName);
        tvFileName.setText(filename);


        Button btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);


        final TextInputLayout textPasswordWrapper = (TextInputLayout) findViewById(R.id.etPasswordWrapper);
        textPasswordWrapper.setHint("Password");
        // end


        // Decrypt button
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = textPasswordWrapper.getEditText().getText().toString();

                if (password.isEmpty()) {
                    textPasswordWrapper.setError("Enter Password");
                } else {
                    // Filepath pick from android device
                    final String filepath = "/storage/emulated/0/Vault/" + filename;


                    final String pasHash = password + "#";

                    String vaultPath = Environment.getExternalStorageDirectory().getPath() + "/Vault/";
                    String decryptedPath = Environment.getExternalStorageDirectory().getPath() + "/decrypted/";

                    Settings settingsActivity = new Settings();
                    String encryptionType = settingsActivity.getDefaultAlgo(getApplicationContext());
                    final Encryption encryption = new Encryption(encryptionType, vaultPath, decryptedPath);

                    // Thread process to speedup the decrypting speed when possible.
                    // Also try-catch methods to check any failures
                    progressDialog = ProgressDialog.show(Decrypt.this, "", "Decrypting");
                    new Thread() {
                        public void run() {
                            try {
                                encryption.decrypt(encryption.hashGenerator(pasHash), filepath);
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
            }
        });
    }
}