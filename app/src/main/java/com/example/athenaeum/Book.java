/*
 * Book
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
import java.util.Map;

/**
 * This is a class of book objects that have an ISBN, author, title, status, and owner,
 * and may also have a description and/or photo.
 */

public class Book implements Serializable {
    private String ISBN;
    private String author;
    private String description;
    private String title;
    private String status;
    private Boolean photo;
    private String ownerUID;
    private String borrowerUID;
    private BookLocation location;
    private ArrayList<String> requesters;


    /**
     * This constructs a Book object from a given ISBN, author, and title
     *
     * @param ISBN   This is the ISBN of the book.
     * @param author This is the author of the book.
     * @param title  This is the title of the book.
     */
    public Book(String ISBN, String author, String title) {
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.status = "Available";
        this.requesters = new ArrayList<>();
        this.photo=false;
    }

    public Book() {
    }

    /**
     * This returns the book's ISBN
     *
     * @return Return the ISBN of the book.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * This sets a book's ISBN
     *
     * @param ISBN This is the new ISBN.
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * This returns the book's author
     *
     * @return Return the author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * This sets a book's author
     *
     * @param author This is the new author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * This returns the book's description
     *
     * @return Return the description of the book.
     */
    public String getDescription() {
        return description;
    }

    /**
     * This sets a book's description
     *
     * @param description This is the new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This returns the book's title
     *
     * @return Return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This sets a book's title
     *
     * @param title This is the new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This returns the book's status
     *
     * @return Return the status of the book.
     */
    public String getStatus() {
        return status;
    }

    /**
     * This sets a book's status
     *
     * @param status This is the new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * This returns the book's owner
     *
     * @return Return the ownerUID of the book.
     */
    public String getOwnerUID() {
        return ownerUID;
    }

    /**
     * This sets a book's owner
     *
     * @param ownerUID This is the new owner.
     */
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    /**
     * This gets the UID of a book's borrower.
     *
     * @return Returns the borrower's UID.
     */
    public String getBorrowerUID() {
        return borrowerUID;
    }

    /**
     * This sets the UID of a book's borrower.
     *
     * @param borrowerUID The new UID string to be set.
     */
    public void setBorrowerUID(String borrowerUID) {
        this.borrowerUID = borrowerUID;
    }

    /**
     * This sets the geolocation data for a book using a Map object.
     *
     * @param location The Map location data to be set.
     */
    public void setLocation(Map<String, Object> location) {
        if (location == null || location.size() == 0) {
            this.location = null;
        } else {
            Map<String, Object> values = (Map<String, Object>) location.get("location");
            if (values.size() == 2) {
                double latitude = (double) values.get("latitude");
                double longitude = (double) values.get("longitude");
                this.location = new BookLocation(latitude, longitude);
            } else {
                this.location = null;
            }
        }
    }

    /**
     * This sets the geolocation data for a book using a supplied latitude and longitude.
     *
     * @param lat The latitude double to be set.
     * @param lon The longitude double to be set.
     */
    public void setLocation(double lat, double lon) {
        this.location = new BookLocation(lat, lon);
    }

    /**
     * This gets the location of a book as a BookLocation object.
     *
     * @return Returns a BookLocation.
     */
    public BookLocation getLocation() { return this.location; }


    /**
     * This is used to request a book. If the book is not already borrowed or and no other
     * requests have already been accepted, the book's status will be updated to Requested
     * and the UID of the requesting user will be added to the book's list of requesters.
     *
     * @param uid The UID string of the requesting user.
     */
    public void request(String uid) {
        if (!this.getStatus().equals("Borrowed") && !this.getStatus().equals("Accepted")) {
            this.setStatus("Requested");
            if (!this.requesters.contains(uid)) {
                this.requesters.add(uid);
            }
        }
    }

    /**
     * This changes the book's status to accepted. It also clears the requester list and adds
     * the UID of the accepted requester.
     *
     * @param uid The UID string of the accepted user.
     */
    public void accept(String uid) {
        this.setStatus("Accepted");
        this.requesters.clear();
        this.requesters.add(uid);
    }

    /**
     * This changes the book's status to borrowed. It also sets the BorrowerUID of the book
     * to the UID of the user borrowing the book.
     *
     * @param uid The UID string of the borrowing user.
     */
    public void giveBook(String uid) {
        this.setStatus("Borrowed");
        this.setBorrowerUID(uid);
    }

    /**
     * This changes the book's status to available. It clears the requesters list as well.
     */
    public void returnBook() {
        this.requesters.clear();
        this.setStatus("Available");
    }

    /**
     * This acknowledges the return of a book. It sets the book's status to Available and clears
     * the book's borrowerUID.
     */
    public void receiveReturn() {
        this.setStatus("Available");
        this.setBorrowerUID(null);
    }

    /**
     * This confirms whether the given UID matches the UID of an accepted requester. If true, the
     * requesters list is cleared and the borrowerUID is set to the given UID.
     *
     * @param uid The UID string of the borrowing user.
     */
    public void confirm(String uid) {
        if (uid.equals(this.requesters.get(0))) {
            this.requesters.clear();
            this.setBorrowerUID(uid);
            this.setStatus("Borrowed");
        }

    }

    /**
     * This sets the book's photo status to true, denoting that there is an associated photo.
     */
    public void setPhoto() {
        this.photo = true;
    }

    /**
     * This sets the book's photo status to false, denoting that there is not an associated photo.
     */
    public void removePhoto() {
        this.photo = false;
    }

    /**
     * This gets the book's photo status, either true or false.
     *
     * @return Returns the photo status Boolean.
     */
    public Boolean getPhoto() {
        return this.photo;
    }

    /**
     * This returns a list of UIDs representing the requesters of a book.
     *
     * @return Returns an ArrayList containing UID strings.
     */
    public ArrayList<String> getRequesters() {
        return this.requesters;
    }

    /**
     * This removes a particular requester from a book's list of requesters.
     *
     * @param uid The UID string of the requester to be removed.
     */
    public void removeRequester(String uid) {
        this.requesters.remove(uid);
    }
}
