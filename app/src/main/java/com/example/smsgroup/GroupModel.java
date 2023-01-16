package com.example.smsgroup;

import java.util.ArrayList;

public class GroupModel {
    private String name, description, image, uid;
    private ArrayList<String> numbers;

    public GroupModel(String name, String description, String image, String uid, ArrayList<String> numbers) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.uid = uid;
        this.numbers = numbers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public String getUid() {
        return uid;
    }
}
