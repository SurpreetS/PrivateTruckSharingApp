package com.example.week9;


// This line declares the MyDataModel class
public class MyDataModel {


    // These lines declare three instance variables,truckName, description, and image
    String truckName, description;
    int image;

    // This is the constructor for the MyDataModel class which takes in a name, description, and image parameter
    public MyDataModel(String name, String description, int image) {
        // This line initializes the truckName variable with the name parameter
        this.truckName = name;
        // This line initializes the description variable with the description parameter
        this.description = description;
        // This line initializes the image variable with the image parameter
        this.image = image;
    }

    // This method returns the value of the truckName instance variable
    public String getTruckName() {
        return truckName;
    }

    // This method returns the value of the description instance variable
    public String getDescription() {
        return description;
    }

    // This method returns the value of the image instance variable
    public int getImage() {
        return image;
    }
}
