/*
 * UserDB
 *
 * November 30 2020
 *
 * Copyright 2020 Natalie Iwaniuk, Harpreet Saini, Jack Gray, Jorge Marquez Peralta, Ramana Vasanthan, Sree Nidhi Thanneeru
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
     *
     * @param username This is the unique username of the profile
     * @param name     This is the name of the user who owns the profile
     * @param password This is the user's password
     * @param phoneNum This is the user's phone number
     * @param email    This is the user's email address
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
     *
     * @param username This is the unique username of the profile
     * @param password This is the user's password
     * @param email    This is the user's email address
     */
    public AthenaeumProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * This constructor is required for firestore.
     */
    public AthenaeumProfile() {
    }

    /**
     * This gets the profile username.
     *
     * @return Returns the profile username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * This sets the profile username.
     *
     * @param username The string to use as the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This gets the profile name.
     *
     * @return Returns the profile name.
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the profile name.
     *
     * @param name The string to use as the new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This gets the profile password.
     *
     * @return Returns the profile password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * This sets the profile password.
     *
     * @param password The string to use as the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This gets the profile phone number.
     *
     * @return Returns the profile phone number.
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * This sets the profile phone number.
     *
     * @param phoneNum The string to use as the new profile phone number.
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * This gets the profile email address.
     *
     * @return Returns the profile email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * This sets the profile email address.
     *
     * @param email The string to use as the new profile email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
