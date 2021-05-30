package com.example.hund_hunter.data_classes;

import android.graphics.Bitmap;

public class Order {
    String email;
    String price;
    String comment;
    String coord;
    String time;
    String image;

    public Order() {}

    public Order(String email, String price, String comment, String coord, String time, String image) {
        this.email = email;
        this.price = price;
        this.comment = comment;
        this.coord = coord;
        this.time = time;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }
}
