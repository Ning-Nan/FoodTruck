package com.miles.foodtruck.Model;


import com.miles.foodtruck.Model.Abstract.AbstractTrackable;

public class FoodTruck extends AbstractTrackable {


    public FoodTruck(int id, String name, String description, String url, String category) {
        super(id, name, description, url, category);
    }
}
