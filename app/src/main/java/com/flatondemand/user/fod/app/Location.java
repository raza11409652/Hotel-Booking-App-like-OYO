package com.flatondemand.user.fod.app;

public class Location {
    private String name;
    private int Image;

    public Location(String name, int image) {
        this.name = name;
        Image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return Image;
    }
}

