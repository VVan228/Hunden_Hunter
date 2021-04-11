package com.example.hund_hunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SeekerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Marker marker;
    private GoogleMap mMap;
    private LatLng coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng myPlace = new LatLng(52.27537, 104.2774);
        //mMap.addMarker(new MarkerOptions().position(myPlace).title("здесь"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
        CameraPosition cameraPosition = new CameraPosition(myPlace, 23, 0, 15);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                TextView textView=(TextView) findViewById(R.id.bottom_sheet);
                /*textView = data time + discription + etc */

            }
        });
    }




    public void onSubmit(View view) {
        Intent intent = new Intent();
        intent.putExtra("coords", coords.toString());
        setResult(1, intent);
        finish();
    }


    public void createMarker(String string){
        String[] latlong =  string.replaceAll("^[0-9;]","").split(";");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng location = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(location));

    }
}