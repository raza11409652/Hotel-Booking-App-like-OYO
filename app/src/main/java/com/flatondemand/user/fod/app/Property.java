package com.flatondemand.user.fod.app;

public class Property {
    String title;
    String image;
    String price;

    public Property(String title, String price, String coverImage) {
            this.title=title;
            this.price=price;
            this.image=coverImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
