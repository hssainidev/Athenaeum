/*
 * User
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
import java.util.ArrayList;

/**
 * This is a class of user objects that have an array of books and
 * a connected profile object.
 */

public class User implements Serializable {
    private ArrayList<String> books;
    private AthenaeumProfile profile;

    /**
     * This constructs a User object from a given profile, creating an empty array for
     * the book objects.
     *
     * @param profile This is the connected profile of the user.
     */
    public User(AthenaeumProfile profile) {
        this.profile = profile;
        books = new ArrayList<>();
    }

    public User() {
    }

    /**
     * This returns the user's array of books.
     *
     * @return Return the array of books owned by the user.
     */
    public ArrayList<String> getBooks() {
        return books;
    }

    /**
     * This sets a user's books
     *
     * @param books This is the new array of books.
     */
    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    /**
     * This adds a new book to the user's array of books.
     *
     * @param book This is the new book.
     */
    public void addBook(String book) {
        books.add(book);
    }

    /**
     * This returns the user's connected profile.
     *
     * @return Return the profile of the user.
     */
    public AthenaeumProfile getProfile() {
        return profile;
    }

    /**
     * This sets a user's profile.
     *
     * @param profile This is the new profile.
     */
    public void setProfile(AthenaeumProfile profile) {
        this.profile = profile;
    }
}
