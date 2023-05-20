package com.petx.petx.Model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Pet implements Serializable {
    private String id;
    private String title;
    private String description;
    private String petType;
    private String imageURL;
    private String userId;

    public Pet() {
        // Default constructor required for Firebase Realtime Database
    }

    public Pet(String id, String title, String description, String petType, String imageURL, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.petType = petType;
        this.imageURL = imageURL;
        this.userId = userId;
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

    public String getImageURL() {
        return imageURL;
    }

    public String getUserId() {
        return userId;
    }
}
