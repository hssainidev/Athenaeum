package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng chosenLocation;
    boolean isOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Book book = (Book) getIntent().getSerializableExtra("BOOK");
        final String uid = getIntent().getExtras().getString("UID");
        if (book.getOwnerUID().equals(uid)) {
            isOwner = true;
        } else {
            isOwner = false;
        }
        if (book.getLocation() != null) {
            chosenLocation = book.getLocation().getLocation();
        } else {
            // Default location
            chosenLocation = new LatLng(53.5232189, -113.5263186);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button choose_location_button = (Button) findViewById(R.id.choose_location_button);
        if (!isOwner) {
            choose_location_button.setVisibility(View.GONE);
        } else {
            choose_location_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOwner) {
                        Toast.makeText(MapActivity.this, "You can't edit the location of this book!", Toast.LENGTH_LONG).show();
                    }
                    // return chosen location & set the book's location to it.
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
            @Override
            public void onMarkerDragEnd(Marker marker) {
                chosenLocation = marker.getPosition();
            }
        });
    }
}
