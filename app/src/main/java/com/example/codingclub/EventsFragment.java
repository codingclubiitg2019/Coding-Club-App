package com.example.codingclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {

    private ImageView addEventBtn;
    final private int REQUEST_ADD_EVENT = 99;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Event> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_events, container, false);

        addEventBtn = view.findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                startActivityForResult(intent, REQUEST_ADD_EVENT);
            }

        });


        recyclerView = view.findViewById(R.id.events_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        //adding some items to our list
        eventList = new ArrayList<>();

        eventList.add(
                new Event(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall"
                        ));

        eventList.add(
                new Event(
                        2,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall"
                ));

        eventList.add(
                new Event(
                        3,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall",
                        "Lecture Hall"
                        ));

        //creating recyclerview adapter
        MyAdapter adapter = new MyAdapter(eventList);

//        mAdapter = new MyAdapter(mDataset);
        Log.d("myAdapter", adapter.toString());
        recyclerView.setAdapter(mAdapter);

        return view;
    }


}

