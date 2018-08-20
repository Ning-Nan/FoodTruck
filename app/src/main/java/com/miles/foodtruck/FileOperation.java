package com.miles.foodtruck;

import java.io.InputStream;

public class FileOperation {
    private String fileName;

    public FileOperation(String fileName){
        this.fileName = fileName;
    }

    public void readFromRaw(){
        String file = "raw/" + fileName; // res/raw/test.txt also work.
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
    }
}

