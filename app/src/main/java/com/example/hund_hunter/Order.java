package com.example.hund_hunter;

public class Order {
    String email;
    String price;
    String comment;
    String coord;
    String time;

    public Order() {}

    public Order(String email, String price, String comment, String coord, String time) {
        this.email = email;
        this.price = price;
        this.comment = comment;
        this.coord = coord;
        this.time = time;
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
}
