package com.miles.foodtruck.Model;

public abstract class Trackable {

    private int id;
    private String name;
    private String description;
    private String url;
    private String category;



    public Trackable(int id, String name, String description, String url, String category)
    {

        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return url;
    }
    public String getCategory() {
        return category;
    }

}
