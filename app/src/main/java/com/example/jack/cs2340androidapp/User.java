package com.example.jack.cs2340androidapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2/20/18.
 */

public class User {

    public static List<User> UserList = new ArrayList<User>();

    private String name;
    private String city;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean administrator;

    public User(String name, String city, String email, String phoneNumber, String password, boolean administrator) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.administrator = administrator;
        UserList.add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
