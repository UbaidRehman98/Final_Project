package com.petx.petx.Model;

import java.io.Serializable;

public class Buy implements Serializable {
    private String id;
    private String title;
    private String description;
    private String petType;
    private String imageURL;
    private String userId;
    private String contact;
    private String address;

    private String price;

    public Buy() {
        // Default constructor required for Firebase Realtime Database
    }

    public Buy(String id, String title, String description, String petType, String imageURL, String userId, String contact, String address, String price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.petType = petType;
        this.imageURL = imageURL;
        this.userId = userId;
        this.contact = contact;
        this.address = address;
        this.price = price;

    }

    public String getId() {
        return id;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
