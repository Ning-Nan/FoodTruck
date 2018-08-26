package com.miles.foodtruck.Model.Abstract;

import com.miles.foodtruck.Model.Interface.TrackableInterface;

public abstract class Trackable implements TrackableInterface{

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

    @Override
    public int getId() { return id; }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {

        return "Name: " + getName() + "\n"
                + "Description: " + getDescription() + "\n"
                + "Url: " + getUrl() + "\n"
                + "Category: " + getCategory();
    }
}
