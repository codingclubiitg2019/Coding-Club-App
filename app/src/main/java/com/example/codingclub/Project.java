package com.example.codingclub;

public class Project {
    private String id;
    private String name;
    private String details;
    private String date;
    private String image;
    private String status;

    public Project(String id, Object name, Object details, Object date, Object status, Object image) {
        this.id = id;
        this.name = (name == null) ? "" : name.toString();
        this.details = (details == null) ? "" : details.toString();
        this.date = (date == null) ? "" : date.toString();
        this.image = (image == null) ? "" : image.toString();
        this.status = (status == null) ? "": status.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }
}
