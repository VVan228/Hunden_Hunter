
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.data_classes.User;
import com.example.hund_hunter.fire_classes.interfaces.OnChildRemovedListener;
import com.example.hund_hunter.other_classes.AddressesMethods;
import com.example.hund_hunter.fire_classes.FireDB;
import com.example.hund_hunter.fire_classes.MyChildListenerFactory;
import com.example.hund_hunter.fire_classes.MyQuery;
import com.example.hund_hunter.log_in_activities.ChoiceActivity;
import com.example.hund_hunter.log_in_activities.UserAccountActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeekerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    FireDB db;
    FireDB users;
    LatLng myPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_activity);

        users = new FireDB(new String[]{"users"});
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        myPos = ChoiceActivity.myPlace;
        //записать текущую позицию и обновить бд
        setPlace();
        //получение меток
        updateData();
    }


    void setPlace(){
        Log.d("tag4me", "setPlace start");
        String[]address = AddressesMethods.getLocalityAndPostal(myPos.toString(), this);
        if(address==null){
            Toast.makeText(SeekerActivity.this, "не получилось распознать адрес", Toast.LENGTH_LONG).show();
            return;
        }
        String locality = address[0];
        String postal = address[1];

        db = new FireDB(new String[]{"orders", locality, postal});
        Log.d("tag4me", locality+" "+postal);
    }


    void updateData(){
        db.getData(new MyQuery(db.getRef()).orderBy("time"), new MyChildListenerFactory().addAddedListener((snapshot, previousChildName) -> {
            Order order = snapshot.getValue(Order.class);
            assert order != null;
            String cords = order.getCoord();
            String email = order.getEmail();
            String price = order.getPrice();
            String comment = order.getComment();
            String time = order.getTime();
            String photoStr = order.getImage();
            String pet = order.getPet();


            Map<String, String> markerData = new HashMap<>();
            markerData.put("cords", cords);
            markerData.put("email", email);
            markerData.put("price", price);
            markerData.put("comment", comment);
            markerData.put("time", time);
            markerData.put("photo", photoStr);
            markerData.put("pet", pet);

            Log.d("tag4me", "child added");
            JSONObject markerDataJson = new JSONObject(markerData);
            createMarker(cords, markerDataJson.toString());
        }).addRemovedListener(snapshot -> {
            Order order = snapshot.getValue(Order.class);
            assert order != null;
            Log.d("tag4me1", order.getCoord());
        }).create());
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //слушатель нажатий на маркеры
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(marker -> {
            JSONObject markerData;
            try {
                String tag = Objects.requireNonNull(marker.getTag()).toString();
                markerData = new JSONObject(tag);
            } catch (JSONException e) {
                Map<String, String> info = new HashMap<>();
                info.put("information", e.getMessage());
                markerData = new JSONObject(info);
            }
            TextView address = findViewById(R.id.bottom_sheet_adress);
            TextView reward = findViewById(R.id.rewardInfo);
            TextView pet_name = findViewById(R.id.bottom_sheet_pet);
            TextView comment = findViewById(R.id.bottom_sheet_commentInfo);
            TextView time = findViewById(R.id.bottom_sheet_time);
            TextView owner = findViewById(R.id.bottom_sheet_owner);
            TextView tel = findViewById(R.id.bottom_sheet_tel);
            ImageView photo = findViewById(R.id.bottom_sheet_photo);

            try {
                String addressLine = AddressesMethods.getFullAddressLine(markerData.getString("cords"), this);
                address.setText(addressLine);
                reward.setText(String.format("р.%s", markerData.getString("price")));
                pet_name.setText(markerData.getString("pet"));
                comment.setText(markerData.getString("comment"));
                time.setText(String.format("время - %s", markerData.getString("time")));

                users.getData(new MyQuery(users.getRef()).orderBy("email").equalTo(markerData.getString("email")),
                        new MyChildListenerFactory().addAddedListener((snapshot, previousChildName) -> {
                            User obj = snapshot.getValue(User.class);
                            assert obj != null;
                            owner.setText(String.format("%s %s", obj.getFamilia(), obj.getName()));
                            tel.setText(obj.getTel());
                        }).create());

                if(markerData.getString("photo").equals("")){
                    TextView no_photo = findViewById(R.id.no_foto);
                    no_photo.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.GONE);
                }else{
                    TextView no_photo = findViewById(R.id.no_foto);
                    no_photo.setVisibility(View.GONE);
                    photo.setVisibility(View.VISIBLE);
                    photo.setImageBitmap(stringToBitMap(markerData.getString("photo")));
                }



            } catch (JSONException e) {
                address.setText(R.string.error);
                reward.setText(R.string.error);
                pet_name.setText(R.string.error);
                comment.setText(e.getMessage());
                time.setText(R.string.error);
                owner.setText(R.string.error);
            }
            return true;
        });
        //Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        CameraPosition cameraPosition = new CameraPosition(myPos, 23, 0, 15);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }



    public void createMarker(String string, String tag){
        double[]str = AddressesMethods.getLatLang(string);
        double lat = str[0];
        double lon = str[1];
        Marker a = mMap.addMarker(new MarkerOptions()
                .zIndex(100)
                .position(new LatLng(lat,lon))
                .icon(BitmapDescriptorFactory.defaultMarker(10.0f)));
        a.setTag(tag);
    }

    static Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        myPos = latLng;
        setPlace();
        updateData();
    }

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
}
