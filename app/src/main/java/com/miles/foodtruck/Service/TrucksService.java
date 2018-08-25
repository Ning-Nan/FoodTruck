package com.miles.foodtruck.Service;

import com.miles.foodtruck.Model.FoodTruck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class TrucksService {

private static ArrayList<FoodTruck> foodTrucks = new ArrayList<>();
private static ArrayList<String> categories = new ArrayList<>();

    public static ArrayList<FoodTruck> readTrackableList(InputStream input) throws IOException {

        if (foodTrucks.size() == 0)
        {
            InputStreamReader inputReader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputReader);

            String line;

            while((line = reader.readLine()) != null) {

                String[] splitLine = line.split(",\"");

                //Delete remaining "
                for (int i = 0; i < splitLine.length; i++)
                {
                    splitLine[i] = splitLine[i].replace("\"","");
                }

                int id = Integer.parseInt(splitLine[0]);
                FoodTruck truck = new FoodTruck(id, splitLine[1],
                        splitLine[2],splitLine[3],splitLine[4]);

                foodTrucks.add(truck);
            }

            return foodTrucks;
        }
        else
        {
            return  foodTrucks;
        }

    }

    public static ArrayList<String> getCategories(){

        if (categories.size() == 0)
        {
            for (int i = 0; i < foodTrucks.size(); i++)
            {
                categories.add(foodTrucks.get(i).getCategory());
            }

            //remove all repeat strings.
            HashSet h = new HashSet(categories);
            categories.clear();
            categories.addAll(h);

            //add heading
            categories.add(0,"All Categories");

            return categories;
        }

        else{
            return categories;
        }
    }

    public static ArrayList<FoodTruck> getTrucksInCategory(String category){

        ArrayList<FoodTruck> foodTrucksInCategory = new ArrayList<>();
        for (int i = 0; i < foodTrucks.size(); i++) {

            if (foodTrucks.get(i).getCategory().equals(category))
            {
                foodTrucksInCategory.add(foodTrucks.get(i));
            }

        }

        return foodTrucksInCategory;
    }

    public static ArrayList<FoodTruck> getTrackableList() {
        return foodTrucks;
    }



}