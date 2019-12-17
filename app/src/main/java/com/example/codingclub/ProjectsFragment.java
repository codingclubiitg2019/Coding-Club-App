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


public class ProjectsFragment extends Fragment {

    private ImageView addProjectBtn;
    final private int REQUEST_ADD_PROJECT = 99;

    List<Project> projectList = new ArrayList<>();
    Boolean data_loaded_once = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = new ProjectsAdapter(projectList);
    private RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        addProjectBtn = view.findViewById(R.id.addProjectBtn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProjectActivity.class); // Change to AddProjectActivity.class
                startActivityForResult(intent, REQUEST_ADD_PROJECT);
            }

        });


        if(!data_loaded_once){
            db.collection("projects")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    projectList.add(new Project(document.getId(), data.get("name"), data.get("details"),
                                            data.get("date"), data.get("status"), data.get("image")));
                                    data_loaded_once = true;
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

        recyclerView = view.findViewById(R.id.projects_recycler_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //creating recyclerview adapter
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
