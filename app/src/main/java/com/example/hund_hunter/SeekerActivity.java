package com.example.hund_hunter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //mMap.setOnMarkerClickListener(SeekerActivity.this);


        FirebaseDatabase.getInstance().getReference().child("orders").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Map<String, Map<String,String>> map = (Map<String, Map<String,String>>)task.getResult().getValue();
                    Map<String, Map<String,String>> map2 = new HashMap<>();
                    for (Map.Entry<String, Map<String,String>> entry : map.entrySet()) {
                        map2.put(entry.getKey(), (Map<String, String>)entry.getValue());
                    }
                    for (Map.Entry<String, Map<String,String>> entry : map2.entrySet()) {

                        //Log.d("yy", entry.getValue().get("coord"));
                        createMarker(entry.getValue().get("coord"), entry.getValue().get("email")+"\n"+entry.getValue().get("price"));

                        //Log.d("yy", "Key = " + entry.getKey() + ", Value = " + entry.getValue().get("email"));
                    }
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(SeekerActivity.this, marker.getPosition().toString(), Toast.LENGTH_LONG).show();
                TextView text = findViewById(R.id.bottom_sheet_2);
                text.setText("email: " + marker.getTag().toString().split("\n")[0]);

                TextView text2 = findViewById(R.id.bottom_sheet_3);
                text2.setText("reward: " + marker.getTag().toString().split("\n")[1]);
                return true;
            }
        });
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

                TextView textView=(TextView) findViewById(R.id.bottom_sheet_3);
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


    public void createMarker(String string, String tag){
        /*String res = string.replaceAll("[^0-9,]","");
        String[] latlong =  res.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng location = new LatLng(latitude, longitude);
        Log.d("yy", location.toString());

        mMap.addMarker(new MarkerOptions().position(location));*/
        String from_lat_lng = "";
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(string);
        while(m.find()) {
            from_lat_lng = m.group(1) ;
        }
        String[] gpsVal = from_lat_lng.split(",");
        double lat = Double.parseDouble(gpsVal[0]);
        double lon = Double.parseDouble(gpsVal[1]);
        Marker a = mMap.addMarker(new MarkerOptions().zIndex(100).position(new LatLng(lat,lon)));
        a.setTag(tag);


    }
}