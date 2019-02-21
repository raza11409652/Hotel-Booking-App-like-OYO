package com.flatondemand.user.fod.model;

public class InclusionProperty {
    String text , image;

    public InclusionProperty(){

    }
    public InclusionProperty(String text, String image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
