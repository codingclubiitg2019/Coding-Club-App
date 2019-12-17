package com.example.codingclub;

public class Event {
    private String id;
    private String name;
    private String details;
    private String venue;
    private String time;
    private String date;
    private String image;

    public Event(String id, Object name, Object details, Object venue, Object time, Object date, Object image) {
        this.id = id;
        this.name = (name == null) ? "" : name.toString();
        this.details = (details == null) ? "" : details.toString();
        this.venue = (venue == null) ? "" : venue.toString();
        this.time = (time == null) ? "" : time.toString();
        this.date = (date == null) ? "" : date.toString();
        this.image = (image == null) ? "" : image.toString();
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
