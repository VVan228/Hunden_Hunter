package com.example.hund_hunter.data_classes;

public class User {
    public String name;
    public String familia;
    public String email;
    private String tel;

    public User() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getFamilia() {
        return familia;
    }

    public String getEmail() {
        return email;
    }


    public User(String name, String familia, String email, String tel) {
        this.name = name;
        this.familia = familia;
        this.email = email;
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
