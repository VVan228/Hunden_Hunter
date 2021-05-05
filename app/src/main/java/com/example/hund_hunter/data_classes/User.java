package com.example.hund_hunter.data_classes;

public class User {
    public String name;
    public String familia;
    public String email;
    public String password;
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

    public void setPassword(String password) {
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public User(String name, String familia, String email, String password, String tel) {
        this.name = name;
        this.familia = familia;
        this.email = email;
        this.password = password;
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
