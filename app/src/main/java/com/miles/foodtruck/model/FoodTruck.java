package com.miles.foodtruck.model;


import com.miles.foodtruck.model.abstracts.AbstractTrackable;

public class FoodTruck extends AbstractTrackable {


    public FoodTruck(int id, String name, String description, String url,
                     String category) {
        super(id, name, description, url, category);
    }
}
