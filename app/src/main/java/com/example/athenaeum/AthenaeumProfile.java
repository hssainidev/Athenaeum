package com.example.athenaeum;

import java.io.Serializable;

/**
 * This is a class of profile objects with a username, name, password,
 * phone number, and email address.
 */

public class AthenaeumProfile implements Serializable {
    private String username;
    private String name;
    private String password;
    private String phoneNum;
    private String email;

    /**
     * This constructs a new profile with the given parameters
     * @param username
     * This is the unique username of the profile
     * @param name
     * This is the name of the user who owns the profile
     * @param password
     * This is the user's password
     * @param phoneNum
     * This is the user's phone number
     * @param email
     * This is the user's email address
     */
    public AthenaeumProfile(String username, String name, String password, String phoneNum, String email) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    /**
     * This constructs a new profile with the given parameters
     * @param username
     * This is the unique username of the profile
     * @param password
     * This is the user's password
     * @param email
     * This is the user's email address
     */
    public AthenaeumProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
