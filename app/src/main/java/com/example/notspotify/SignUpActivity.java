package com.example.notspotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText inputUserName = (EditText) findViewById(R.id.text_input_uSignUp);
        final EditText inputPassword = (EditText) findViewById(R.id.text_input_pSignUp);
        final Button loginButton = (Button) findViewById(R.id.button_signUp);

        final UserList userList = loadJsonIntoUserList();

        final String path = getFilesDir().getAbsolutePath() + "/users.json";
        final File file = new File(path);
        final UserList newUserList =  updateUserList(file);

    }

    public UserList updateUserList(File file)
    {
        UserList userTemp = null;
        try{


            if (file.exists()) {
                //Log.d("ADDUSER", "file exists");
                InputStream inputStream = new FileInputStream(file);
                String myJson = inputStreamToString(inputStream);
                userTemp = new Gson().fromJson(myJson, UserList.class);
                inputStream.close();

                //Log.d("ADDUSER", userList.toString());

                return userTemp;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userTemp;
    }

    public void addUser(String name, String password, String json , UserList userlist)
    {
        //Log.d("ADDUSER", "adding user");
        //Log.d("ADDUSER", getFilesDir().getPath());

        User newUser = new User();
        newUser.setUserName(name);
        newUser.setPassword(password);
        newUser.setJson(json);

        userlist.addToList(newUser);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(userlist);

        //String filename = "users.json";
        String fileContents = strJson;
        FileOutputStream outputStream;

        try {
            String filePath =   getFilesDir().getAbsolutePath() + "/users.json";
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strJson.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //Log.d("ADDUSER", "OUTPUTTED");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads the users from the users.json file into userlist object using GSON
     * @return the populated user list
     */
    public UserList loadJsonIntoUserList()
    {
        try
        {
            String myJson = inputStreamToString(getAssets().open("users.json"));
            UserList userList  = new Gson().fromJson(myJson, UserList.class);
            return userList;
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a file using inputstream
     * @param inputStream a file to read from
     * @return a string of the read in file
     */
    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
