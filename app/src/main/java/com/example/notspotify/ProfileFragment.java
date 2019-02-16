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
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Set username
        final Session session = new Session(getActivity());
        username = view.findViewById(R.id.tvName);
        username.setText(session.getUsername());

        // Sign Out button listener
        Button signout = view.findViewById(R.id.button_sign_out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getMediaPlayer().isPlaying())
                {
                    session.getMediaPlayer().stop();
                    session.getMediaPlayer().reset();
                    session.getMediaPlayer().release();
                    session.setMediaPlayer(null);
                }
                Session session;//global variable
                session = new Session(getContext());
                session.setLoginFalse("Login");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
