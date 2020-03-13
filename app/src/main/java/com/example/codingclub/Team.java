package com.example.codingclub;

import com.google.firebase.firestore.DocumentReference;

public class Team {
    private String id;
    private String year;
    private String position;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private DocumentReference documentReference;

    public Team(String id, Object year, Object position, Object user) {
        this.id = id;
        this.year = (year == null) ? "" : year.toString();
        this.position = (position == null) ? "" : position.toString();
        this.documentReference = (DocumentReference) user;
    }

    public Team() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
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

    public DocumentReference getDocumentReference() {
        return documentReference;
    }
}
