package com.example.notspotify;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.TextView;

/**
 * Profile fragment which includes the user's username and sign out button
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    // The user's username
    TextView mUsername;

    // Button to sign out
    Button mSignOutButton;

    //The session
    Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Create new session
        //session = new Session(getActivity());
        session = MainActivity.getSession();

        // Get and set username
        mUsername = view.findViewById(R.id.tvName);
        mUsername.setText(session.getUsername());

        // Sign Out button listener
        mSignOutButton = view.findViewById(R.id.button_sign_out);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getMediaPlayer() != null) {
                    if(session.getMediaPlayer().isPlaying()) {
                        session.getMediaPlayer().stop();
                        session.getMediaPlayer().reset();
                        session.getMediaPlayer().release();
                        session.setMediaPlayer(null);
                    }
                }
                session.setLoginFalse("Login");
//                session.setUsername(null);
//                session.setPassword(null);

                // Call intent to go back to sign up page
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
