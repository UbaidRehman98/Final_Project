package com.petx.petx.Model;

import java.io.Serializable;

public class PetSales implements Serializable {
    private String id;
    private String title;
    private String description;
    private String petType;
    private String imageURL;
    private String userId;

    private String price;

    public PetSales() {
        // Default constructor required for Firebase Realtime Database
    }

    public PetSales(String id, String title, String description, String petType, String imageURL, String userId, String price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.petType = petType;
        this.imageURL = imageURL;
        this.userId = userId;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPetType() {
        return petType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUserId() {
        return userId;
    }
}
