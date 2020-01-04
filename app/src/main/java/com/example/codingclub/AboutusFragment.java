package com.example.codingclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AboutusFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    final private int REQUEST_ADD_EVENT = 99;
    ImageView addMemberBtn;

    List<Team> teamList = new ArrayList<>();
    List<String> memberNames = new ArrayList<>();
    Boolean data_loaded_once = false;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter = new AboutusAdapter(teamList, memberNames);
    private GridLayoutManager layoutManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);

        addMemberBtn = view.findViewById(R.id.btn_add_member);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEventActivity.class);
                startActivityForResult(intent, REQUEST_ADD_EVENT);
            }

        });

        if(!data_loaded_once){
            db.collection("core_team")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    final Map<String, Object> data = document.getData();

                                    final DocumentReference user_ref = document.getDocumentReference("user");
                                    user_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            if(snapshot != null){
                                                memberNames.add(snapshot.getString("name"));
                                                teamList.add(new Team(document.getId(), data.get("year"), data.get("position"), user_ref));
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });


                                }
                                data_loaded_once = true;
                            }
                        }
                    });
        }

        recyclerView = view.findViewById(R.id.aboutus_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        //creating recyclerview adapter
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
