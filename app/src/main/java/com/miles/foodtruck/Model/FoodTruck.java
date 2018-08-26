package com.miles.foodtruck.Model;


import com.miles.foodtruck.Model.Abstract.Trackable;

public class FoodTruck extends Trackable {


    public FoodTruck(int id, String name, String description, String url, String category) {
        super(id, name, description, url, category);
    }
}
