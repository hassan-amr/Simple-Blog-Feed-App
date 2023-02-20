package com.hassanamr.simpleblogfeed.data.model;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String website;

    private int profileImageDrawable;

    public User(int id, String name, String email, String phone, String website) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getProfileImageDrawable() {
        return profileImageDrawable;
    }

    public void setProfileImageDrawable(int profileImageDrawable) {
        this.profileImageDrawable = profileImageDrawable;
    }
}