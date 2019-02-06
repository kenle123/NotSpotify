package com.example.notspotify;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {


    public LibraryFragment() {
        // Required empty public constructor
    }

    ListView listView;
    TextView mPlaylistUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        Session session = new Session(getActivity());
        mPlaylistUser = view.findViewById(R.id.textview_playlists);
        mPlaylistUser.setText(session.getUsername() + "'s Playlist");

        listView = view.findViewById(R.id.list_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("Playlist1");
        list.add("Playlist2");
        list.add("Playlist3");
        list.add("Playlist4");
        list.add("Playlist5");
        list.add("Playlist6");
        list.add("Playlist7");
        list.add("Playlist8");
        list.add("Playlist9");
        list.add("Playlist10");
        list.add("Playlist11");
        list.add("Playlist12");
        list.add("Playlist13");
        list.add("Playlist14");
        list.add("Playlist15");
        list.add("Playlist16");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "i is " + i, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
