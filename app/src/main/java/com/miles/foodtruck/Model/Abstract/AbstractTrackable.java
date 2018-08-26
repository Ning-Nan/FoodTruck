package com.miles.foodtruck.Model.Abstract;

import com.miles.foodtruck.Model.Interface.TrackableInterface;

public abstract class AbstractTrackable implements TrackableInterface{

    private int id;
    private String name;
    private String description;
    private String url;
    private String category;



    public AbstractTrackable(int id, String name, String description, String url, String category)
    {

        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
    }

    public int getId() { return id; }
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

    @Override
    public String getOutPutString() {

        return "Name: " + getName() + "\n"
                + "Description: " + getDescription() + "\n"
                + "Url: " + getUrl() + "\n"
                + "Category: " + getCategory();
    }
}
