package com.example.hund_hunter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeekerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Marker marker;
    private GoogleMap mMap;
    private LatLng coords;
    FireDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);

        db = new FireDB(new String[]{"orders"});
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mMap.setOnMarkerClickListener(SeekerActivity.this);

        //получить метки
        db.getData(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                String coords = order.getCoord();
                String email = order.getEmail();
                String price = order.getPrice();
                String comment = order.getComment();
                String time = order.getTime();

                Map<String, String> markerData = new HashMap<>();
                markerData.put("coords", order.getCoord());
                markerData.put("email", order.getEmail());
                markerData.put("price", order.getPrice());
                markerData.put("comment", order.getComment());
                markerData.put("time", order.getTime());

                JSONObject markerDataJson = new JSONObject(markerData);
                createMarker(coords, markerDataJson.toString());
                Log.d("tag4me", markerDataJson.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }, new myQuery(db.getRef()).orderBy("time"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //слушатель нажатий на маркеры
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(SeekerActivity.this, marker.getPosition().toString(), Toast.LENGTH_LONG).show();
                JSONObject markerData = null;
                try {
                    markerData = new JSONObject(marker.getTag().toString());
                } catch (JSONException e) {
                    Map<String, String> info = new HashMap<>();
                    info.put("information", e.getMessage());
                    markerData = new JSONObject(info);
                }
                TextView text = findViewById(R.id.bottom_sheet_2);
                try {
                    text.setText(markerData.getString("email"));
                } catch (JSONException e) {
                    text.setText("error " + e.getMessage());
                }

                TextView text2 = findViewById(R.id.bottom_sheet_3);
                text2.setText(markerData.toString());
                return true;
            }
        });
        //Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng myPlace = new LatLng(52.27537, 104.2774);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
        CameraPosition cameraPosition = new CameraPosition(myPlace, 23, 0, 15);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void onSubmit(View view) {
        Intent intent = new Intent();
        intent.putExtra("coords", coords.toString());
        setResult(1, intent);
        finish();
    }

    public void createMarker(String string, String tag){
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