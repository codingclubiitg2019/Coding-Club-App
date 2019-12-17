package com.example.codingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventsFragment extends Fragment {

    private ImageView addEventBtn;
    final private int REQUEST_ADD_EVENT = 99;

    List<Event> eventList = new ArrayList<>();
    Boolean data_loaded_once = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = new EventAdapter(eventList);
    private RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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


        if(!data_loaded_once){
            db.collection("events")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    eventList.add(new Event(document.getId(), data.get("name"), data.get("details"),
                                            data.get("venue"), data.get("time"), data.get("date"), data.get("image")));
                                    data_loaded_once = true;
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        recyclerView = view.findViewById(R.id.events_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //creating recyclerview adapter

        recyclerView.setAdapter(mAdapter);

        return view;
    }


}

