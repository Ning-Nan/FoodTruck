package com.miles.foodtruck.Model;

public abstract class AbstractTrackable{

    private int id;
    private String name;
    private String description;
    private String url;
    private String  category;

    public AbstractTrackable(int id, String name, String description, String url, String category)
    {

        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
    }


}
