package com.miles.foodtruck.Service;

import android.content.Intent;

import com.miles.foodtruck.Model.FoodTruck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileReader {

private static ArrayList<FoodTruck> foodTrucks;

    public static ArrayList<FoodTruck> getTrackableList(InputStream input) throws IOException {

        if (foodTrucks.size() == 0)
        {
            InputStreamReader inputReader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputReader);

            String line;

            while((line = reader.readLine()) != null) {

                String[] splitLine = line.split(",\"");

                //Delete remaining ","
                for (int i = 0; i < splitLine.length; i++)
                {
                    splitLine[i].replace(",","");
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

}