package com.example.notspotify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import android.widget.Button;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputUserName = (EditText) findViewById(R.id.text_input_username);
        final EditText inputPassword = (EditText) findViewById(R.id.text_input_password);
        final Button loginButton = (Button) findViewById(R.id.button_signIn);

        final UserList userList = loadJsonIntoUserList();
        final MusicList musicList = loadJsonIntoMusicList();
        //Log.d("MUSICLIST", musicList.toString());

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Log.d("ONCLICKUSER", inputUserName.getText().toString());
                //Log.d("ONCLICKPASSWOR",inputPassword.getText().toString());
                //Log.d("USERLIST", userList.toString());
                checkCredentials(inputUserName.getText().toString(), inputPassword.getText().toString(), userList.getList(), v);

            }

        });
    }

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

    public MusicList loadJsonIntoMusicList()
    {

        try
        {
            String myJson = inputStreamToString(getAssets().open("music.json"));
            MusicList musicList  = new Gson().fromJson(myJson, MusicList.class);
            return musicList;
        }
        catch (IOException e) {
            return null;
        }
    }

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

    public void checkCredentials(String username, String password, List<User> userlist, View v)
    {
        for (int i = 0; i < userlist.size(); i++)
        {

            if(username.equals(userlist.get(i).getUserName()) && password.equals(userlist.get(i).getPassword()))
            {
                signIn(v);
            }

        }
    }

    public void signIn(View view) {
        Intent intent = new Intent(this, BottomNavActivity.class);
        startActivity(intent);
    }
}
