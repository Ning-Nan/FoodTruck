package com.miles.foodtruck.Model;

public abstract class AbstractTrackable{

    private int id;
    private String name;
    private String description;
    private String url;
    private String[]  category;



    public AbstractTrackable(int id, String name, String description, String url, String[] category)
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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

}
