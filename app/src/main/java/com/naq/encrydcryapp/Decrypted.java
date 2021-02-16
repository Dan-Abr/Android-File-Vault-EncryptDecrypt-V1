package com.naq.encrydcryapp;
//this activity of code is for when decryption process is performed and then the decryped files listed in the app

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class Decrypted extends AppCompatActivity{
    // variable initialization
    AlertDialog dialog;
    private ListView mListView;
    private ArrayAdapter<String> directoryList;
    private EditText editTextSearch;
    private TextView textViewVault;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decypted);
        // Type Casting
        // We want to cast it as DrawerLayout because we may want to call methods on the instance that are defined in that class.
        // Since Java is a type-safe language, the compiler can be sure that an object is of a certain class, it won't let us call methods (or access fields) for that class.
        // start
        editTextSearch = (EditText) findViewById(R.id.editSearch);
        textViewVault = (TextView) findViewById(R.id.textViewVault);

        textViewVault.setVisibility(View.GONE);
        //end

        // FileManager activity object is for we use to pick the file from storage like image,video,document etc.
        final FileManager fileManager = new FileManager();

        // filepath from storage
        File path = new File(Environment.getExternalStorageDirectory().getPath()+"/Decrypted/");
        final ArrayList<String> fileList = fileManager.ListDir(path);
        mListView = (ListView) findViewById(R.id.listView);


       // condition to list all decrypted files, else show a message of empty
        if (fileList.size() != 0) {

            directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
            mListView.setAdapter(directoryList);

            textViewVault.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "No Files are Decrypted", Toast.LENGTH_SHORT).show();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int j, long l) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(Decrypted.this);

                // Set the alert dialog title
                builder.setTitle("please select");




                // Initializing an array of choice
                final String[] vault_options = new String[]{
                        "View file",
                        "Delete File",
                };


                // Set a set items list for alert dialog
                builder.setItems(vault_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        // Get the alert dialog selected item's text
                        String selectedItem = Arrays.asList(vault_options).get(i);

                        // Display the selected item's text on snack bar
                        if (selectedItem.equalsIgnoreCase("View File")) {

                            String filename = (String) adapterView.getItemAtPosition(j);
                            Viewfile(filename);
                            filename = null;

                        }  else if (selectedItem.equalsIgnoreCase("Delete File")) {

                            String filename = (String) adapterView.getItemAtPosition(j);
                            DeleteFile(filename);
                            filename = null;


                        }
                    }
                });

                // Create the alert dialog
                dialog = builder.create();
                // Finally, display the alert dialog
                dialog.show();
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the text
                Decrypted.this.directoryList.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


    }
      // Delete file method
    private void DeleteFile(final String filename) {

        dialog.dismiss();
        AlertDialog.Builder a_builder = new AlertDialog.Builder(Decrypted.this);
        a_builder.setTitle("Are you sure want to Delete?");
        a_builder.setMessage("Deleted file cannot be Restore be careful")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String filePath = Environment.getExternalStorageDirectory().getPath() + "/Decrypted/" + filename;
                        File file = new File(filePath);
                        if (file.delete()) {
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            refreshList();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error while deleting", Toast.LENGTH_SHORT).show();

                        }


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
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }
    // refreshing the list after deleting or storing the file
    public void refreshList() {

        FileManager fileManager = new FileManager();
        // Getting file path to Vault's folder
        File path = new File(Environment.getExternalStorageDirectory().getPath() + "/Decrypted/");

        // This snippet gets the file list available from Vault's folder
        ArrayList<String> fileList = fileManager.ListDir(path);

        directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
        mListView.setAdapter(directoryList);
    }
   // viewfile code
    private void Viewfile(String filename) {


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int j, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Decrypted"));

                intent.setDataAndType(uri, "*/*");
                startActivity(Intent.createChooser(intent, "Decrypted"));






            }
        });


    }
    // if we search the file via voice based search
    public void btnSpeech(View view) {


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, 1);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextSearch.setText(result.get(0));

                }
                break;
        }


    }








}