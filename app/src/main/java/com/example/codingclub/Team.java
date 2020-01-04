package com.example.codingclub;

import com.google.firebase.firestore.DocumentReference;

public class Team {
    private String id;
    private String year;
    private String position;
    private DocumentReference user_ref;

    public Team(String id, Object year, Object position, Object user) {
        this.id = id;
        this.year = (year == null) ? "" : year.toString();
        this.position = (position == null) ? "" : position.toString();
        this.user_ref = (DocumentReference) user;
    }

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getPosition() {
        return position;
    }

    public DocumentReference getUserRef() {
        return user_ref;
    }
}
