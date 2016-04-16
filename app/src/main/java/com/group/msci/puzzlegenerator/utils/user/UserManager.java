package com.group.msci.puzzlegenerator.utils.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.AlertDialog;
import com.group.msci.puzzlegenerator.MainActivity;
import com.group.msci.puzzlegenerator.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by filipt on 15/04/2016.
 */
public class UserManager extends Activity {

    public final static String STORAGE_FILE = "username";
    private final static String ALNUM_PATTERN = "[A-Za-z0-9]+";


    private EditText unameField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_register);

        unameField = (EditText) findViewById(R.id.uname_field);
        registerButton = (Button) findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String username = unameField.getText().toString();
                Log.i("UserManager", username);
               if (validateUname(username)) {
                   File unameFile = new File(getApplicationContext().getFilesDir(), STORAGE_FILE);
                   unameFile.mkdirs();
                   Log.i("UserManager", username);
                   uploadUser(username);
                   writeUsername(username);
                   Intent intent = new Intent(UserManager.this, MainActivity.class);
                   UserManager.this.startActivity(intent);
               }
               else {
                   showInvalidAlert();
               }

            }
        });
    }

    private void writeUsername(String username) {
        try {
            OutputStreamWriter os = new OutputStreamWriter(openFileOutput(STORAGE_FILE,
                                                                           Context.MODE_PRIVATE));
            os.write(username);
            os.close();
        } catch (IOException e) {

        }
    }

    public static String getCurrentUsername(Context ctx) {
        StringBuilder sb = new StringBuilder();
       try {
           BufferedReader br = new BufferedReader(new InputStreamReader(
                                                  ctx.openFileInput(STORAGE_FILE)));
           String line;
           while ((line = br.readLine()) != null) {
               sb.append(line);
           }

       } catch (IOException e) {

       }
        return sb.toString();
    }

    private void uploadUser(String username) {
        (new Thread(new UserUploader(username))).start();
    }

    private void showInvalidAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Invalid Username");
        alertDialog.setMessage("Username must not already exist, and can only contain alphanumeric characters.");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private boolean validateUname(String uname) {
        //Match against list of existing users.
        UserListRetriever retriever = new UserListRetriever();
        Thread listRetriever = new Thread(retriever);
        listRetriever.start();

        try {
            listRetriever.join();
        } catch (InterruptedException e) {

        }
        JSONArray userList = retriever.getUserList();
        return uname.matches(ALNUM_PATTERN) && !userList.toString().contains(uname);
    }


    //Convenicence for testing
    public static void deleteStorageFile(Context ctx) {
        File file = new File(ctx.getFilesDir(), STORAGE_FILE);
    }
}
