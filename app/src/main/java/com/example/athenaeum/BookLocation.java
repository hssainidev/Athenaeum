/*
 * BookLocation
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

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * This class stores a latitude and a longitude for a pickup location.
 */
public class BookLocation implements Serializable {
    private double lat;
    private double lon;

    /**
     * Blank constructor necessary to work with Firestore
     */
    public BookLocation() { }

    /**
     * Constructor using a given latitude and longitude
     * @param lat Given latitude
     * @param lon Given longitude
     */
    public BookLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Setter for the latitude and longitude
     * @param lat Given latitude
     * @param lon Given longitude
     */
    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Getter for the location in LatLng format
     * @return Returns a LatLng coordinate of the pickup location.
     */
    public LatLng getLocation() {
        return new LatLng(lat, lon);
    }
}
