package com.example.codingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;
import java.util.Objects;


public class AboutusFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    final private int REQUEST_ADD_EVENT = 99;
    ImageView addMemberBtn;

    ArrayList<Team> teamList = new ArrayList<>();
    ArrayList<String> memberNames = new ArrayList<>();


    private RecyclerView aboutusRecyclerView;
    private AboutusAdapter aboutusAdapter = new AboutusAdapter(teamList, memberNames);
    private GridLayoutManager layoutManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);


        addMemberBtn = view.findViewById(R.id.btn_add_member);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAboutusActivity.class);
                startActivityForResult(intent, REQUEST_ADD_EVENT);
            }

        });


        memberNames.clear();
        teamList.clear();


        db
                .collection("core_team")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                            {
                                final Team data = document.toObject(Team.class);

                                DocumentReference user_ref = document.getDocumentReference("documentReference");

                                assert user_ref != null;
                                user_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot)
                                    {
                                        if(snapshot != null){
                                            memberNames.add(snapshot.getString("name"));
                                            teamList.add(data);
                                            aboutusAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });


                            }

                        }
                    }
                });

        aboutusRecyclerView = view.findViewById(R.id.aboutus_recycler_view);

        layoutManager = new GridLayoutManager(view.getContext(), 2);
        aboutusRecyclerView.setLayoutManager(layoutManager);

        aboutusRecyclerView.setAdapter(aboutusAdapter);
        return view;
    }
}
