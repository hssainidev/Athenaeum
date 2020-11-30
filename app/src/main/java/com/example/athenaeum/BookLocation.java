package com.example.athenaeum;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class BookLocation implements Serializable {
    private double lat;
    private double lon;

    public BookLocation() { }

    public BookLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lon);
    }
}
