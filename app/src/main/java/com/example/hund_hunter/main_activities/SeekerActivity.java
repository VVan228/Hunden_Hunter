
package com.example.hund_hunter.main_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.myQuery;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            Toast.makeText(SeekerActivity.this, "profile lol", Toast.LENGTH_LONG).show();
            //вызвать окно профиля
            //Intent intent = new Intent(this, SearchUsersActivity.class);
            //startActivity(intent);
        }
        return true;
    }

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
                TextView email = findViewById(R.id.emailInfo);
                try {
                    email.setText(markerData.getString("email"));
                } catch (JSONException e) {
                    email.setText("error " + e.getMessage());
                }

                TextView reward = findViewById(R.id.rewardInfo);
                try {
                    reward.setText(markerData.getString("price"));
                } catch (JSONException e) {
                    reward.setText("error " + e.getMessage());
                }

                TextView comment = findViewById(R.id.commentInfo);
                try {
                    comment.setText(markerData.getString("comment"));
                } catch (JSONException e) {
                    comment.setText("error " + e.getMessage());
                }

                TextView time = findViewById(R.id.timeInfo);
                try {
                    time.setText(markerData.getString("time"));
                } catch (JSONException e) {
                    time.setText("error " + e.getMessage());
                }
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
