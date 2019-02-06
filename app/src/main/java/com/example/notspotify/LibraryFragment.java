package com.example.notspotify;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    //List list = new ArrayList();
    //ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        listView = view.findViewById(R.id.list_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("Song");
        list.add("Song1");
        list.add("Song2");
        list.add("Song3");
        list.add("Song4");
        list.add("Song5");
        list.add("Song6");
        list.add("Song7");
        list.add("Song8");
        list.add("Song9");
        list.add("Song10");
        list.add("Song11");
        list.add("Song12");
        list.add("Song13");
        list.add("Song14");
        list.add("Song15");
        list.add("Song16");

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
