
package com.example.hund_hunter.main_activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.data_classes.User;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.MyChildListenerFactory;
import com.example.hund_hunter.fire_classes.MyQuery;
import com.example.hund_hunter.fire_classes.interfaces.OnChildAddedListener;
import com.example.hund_hunter.log_in_activities.UserAccountActivity;
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
    FireDB users;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            //вызов окна профиля
            Intent intent = new Intent(SeekerActivity.this, UserAccountActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);

        db = new FireDB(new String[]{"orders"});
        users = new FireDB(new String[]{"users"});
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mMap.setOnMarkerClickListener(SeekerActivity.this);

        //получение меток
        db.getData(new MyQuery(db.getRef()).orderBy("time"), new MyChildListenerFactory().addAddedListener(new OnChildAddedListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                String coords = order.getCoord();
                String email = order.getEmail();
                String price = order.getPrice();
                String comment = order.getComment();
                String time = order.getTime();
                String photoStr = order.getImage();
                String pet = order.getPet();


                Map<String, String> markerData = new HashMap<>();
                markerData.put("coords", coords);
                markerData.put("email", email);
                markerData.put("price", price);
                markerData.put("comment", comment);
                markerData.put("time", time);
                markerData.put("photo", photoStr);
                markerData.put("pet", pet);

                JSONObject markerDataJson = new JSONObject(markerData);
                createMarker(coords, markerDataJson.toString());
                Log.d("tag4me", markerDataJson.toString());
            }
        }).create());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //слушатель нажатий на маркеры
        mMap.setOnMarkerClickListener(marker -> {
            //Toast.makeText(SeekerActivity.this, marker.getPosition().toString(), Toast.LENGTH_LONG).show();
            JSONObject markerData = null;
            try {
                markerData = new JSONObject(marker.getTag().toString());
            } catch (JSONException e) {
                Map<String, String> info = new HashMap<>();
                info.put("information", e.getMessage());
                markerData = new JSONObject(info);
            }
            TextView adress = findViewById(R.id.bottom_sheet_adress);
            TextView reward = findViewById(R.id.rewardInfo);
            TextView pet_name = findViewById(R.id.bottom_sheet_pet);
            TextView comment = findViewById(R.id.bottom_sheet_commentInfo);
            TextView time = findViewById(R.id.bottom_sheet_time);
            TextView owner = findViewById(R.id.bottom_sheet_owner);
            TextView tel = findViewById(R.id.bottom_sheet_tel);
            ImageView photo = findViewById(R.id.bottom_sheet_photo);

            try {
                adress.setText(markerData.getString("email"));
                reward.setText(markerData.getString("price"));
                pet_name.setText(markerData.getString("pet"));
                comment.setText(markerData.getString("comment"));
                time.setText("Время: " + markerData.getString("time"));

                users.getData(new MyQuery(users.getRef()).orderBy("email").equalTo(markerData.getString("email")),
                        new MyChildListenerFactory().addAddedListener((snapshot, previousChildName) -> {
                            User obj = snapshot.getValue(User.class);
                            owner.setText("Владелец: " + obj.getFamilia() + " " + obj.getName());
                            tel.setText(obj.getTel());
                        }).create());


                photo.setImageBitmap(stringToBitMap(markerData.getString("photo")));


            } catch (JSONException e) {
                adress.setText("error " + e.getMessage());
                reward.setText("error " + e.getMessage());
                pet_name.setText("error " + e.getMessage());
                comment.setText("error " + e.getMessage());
                time.setText("error " + e.getMessage());
                owner.setText("error " + e.getMessage());
            }
            return true;
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

    Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
