package com.example.codingclub;

public class Event {
    private int id;
    private String name;
    private String details;
    private String venue;
    private String time;
    private String date;
    private String image;

    public Event(int id, String name, String details, String venue, String time, String date, String image) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.venue = venue;
        this.time = time;
        this.date = date;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }
}
