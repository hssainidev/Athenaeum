/*
 * MapActivity
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
 *
 * Referenced for Map
 * https://developers.google.com/maps/documentation/android-sdk/overview
 * by Google Developers
 * Published November 19, 2020
 * Licensed under the Apache 2.0 License.
 */

package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This Activity allows a user to view a pickup location for a book.
 * If the user owns the book, they can change the location.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng chosenLocation;
    private boolean isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Retrieve the book and uid.
        final Book book = (Book) getIntent().getSerializableExtra("BOOK");
        final String uid = getIntent().getExtras().getString("UID");

        // Set the isOwner boolean based on if the current user is the owner.
        if (book.getOwnerUID().equals(uid)) {
            isOwner = true;
        } else {
            isOwner = false;
        }

        if (book.getLocation() != null) {
            // Set the location to the book's location if it exists.
            chosenLocation = book.getLocation().getLocation();
        } else {
            // Default location
            chosenLocation = new LatLng(53.5232189, -113.5263186);
        }

        // Initialize the toolbar.
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.map_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView toolbar_title = findViewById(R.id.map_toolbar_title);
        toolbar_title.setText(book.getTitle() + "'s Location");

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the choose location button.
        Button choose_location_button = (Button) findViewById(R.id.choose_location_button);
        if (!isOwner) {
            // Hide the button if the user is not the owner.
            choose_location_button.setVisibility(View.GONE);
        } else {
            choose_location_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ensure that the non-owner cannot edit the location, even if they manage to click the button.
                    if (!isOwner) {
                        Toast.makeText(MapActivity.this, "You can't edit the location of this book!", Toast.LENGTH_LONG).show();
                    }
                    // Return chosen location & set the book's location to it.
                    Intent intent = new Intent();
                    intent.putExtra("LOCATION", chosenLocation);
                    setResult(1, intent);
                    finish();
                }
            });
        }
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Edmonton, Alberta, Canada,
        // and move the map's camera to the same location.
        googleMap.addMarker(new MarkerOptions()
                .position(chosenLocation)
                .draggable(isOwner)
                .title("Pickup Location"));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(chosenLocation));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }
            @Override
            public void onMarkerDrag(Marker marker) {
            }
            // If the marker is dragged, set the chosen location to the current one.
            @Override
            public void onMarkerDragEnd(Marker marker) {
                chosenLocation = marker.getPosition();
            }
        });
    }
}
