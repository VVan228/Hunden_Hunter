
package com.example.hund_hunter.main_activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.hund_hunter.log_in_activities.ChoiceActivity;
import com.example.hund_hunter.log_in_activities.UserAccountActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeekerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private Marker marker;
    private GoogleMap mMap;
    private LatLng coords;
    FireDB db;
    FireDB users;
    LatLng myPos;

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



    void setPlace(){
        Log.d("tag4me", "setPlace start");
        String[]adress = SeekerActivity.getAdress(myPos.toString(), this);
        if(adress==null){
            Toast.makeText(SeekerActivity.this, "не получилось распознать адрес", Toast.LENGTH_LONG).show();
            return;
        }
        String locality = adress[0];
        String postal = adress[1];
        db = new FireDB(new String[]{"orders", locality, postal});
        Log.d("tag4me", locality+" "+postal);
    }

    void updateData(){
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

                Log.d("tag4me", "child added");
                JSONObject markerDataJson = new JSONObject(markerData);
                createMarker(coords, markerDataJson.toString());
            }
        }).create());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_activity);

        users = new FireDB(new String[]{"users"});
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mMap.setOnMarkerClickListener(SeekerActivity.this);

        //Intent intent = getIntent();
        //double lat = intent.getDoubleExtra("latitude", 52.27537);
        //double lan = intent.getDoubleExtra("longitude", 104.2774);
        myPos = ChoiceActivity.myPlace;//new LatLng(lat, lan);
        setPlace();

        //получение меток
        updateData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //слушатель нажатий на маркеры
        mMap.setOnMapClickListener(this);
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
                String adres = getFullAdress(markerData.getString("coords"), this);
                adress.setText(adres);
                reward.setText("р." + markerData.getString("price"));
                pet_name.setText(markerData.getString("pet"));
                comment.setText(markerData.getString("comment"));
                time.setText("время - " + markerData.getString("time"));

                users.getData(new MyQuery(users.getRef()).orderBy("email").equalTo(markerData.getString("email")),
                        new MyChildListenerFactory().addAddedListener((snapshot, previousChildName) -> {
                            User obj = snapshot.getValue(User.class);
                            owner.setText(obj.getFamilia() + " " + obj.getName());
                            tel.setText(obj.getTel());
                        }).create());

                if(markerData.getString("photo").equals("")){
                    TextView no_foto = findViewById(R.id.no_foto);
                    no_foto.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.GONE);
                }else{
                    TextView no_foto = findViewById(R.id.no_foto);
                    no_foto.setVisibility(View.GONE);
                    photo.setVisibility(View.VISIBLE);
                    photo.setImageBitmap(stringToBitMap(markerData.getString("photo")));
                }



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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        CameraPosition cameraPosition = new CameraPosition(myPos, 23, 0, 15);
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
        double[]str = getLatLang(string);
        double lat = str[0];
        double lon = str[1];
        Marker a = mMap.addMarker(new MarkerOptions().zIndex(100).position(new LatLng(lat,lon)));
        a.setTag(tag);
    }

    static Bitmap stringToBitMap(String encodedString){
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

    static double[] getLatLang(String string){
        String from_lat_lng = "";
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(string);
        while(m.find()) {
            from_lat_lng = m.group(1) ;
        }
        String[] gpsVal = from_lat_lng.split(",");
        return new double[]{Double.parseDouble(gpsVal[0]), Double.parseDouble(gpsVal[1])};
    }


    static public String[] getAdress(String latLng, Context context){
        Locale locale = new Locale("en", "US");
        Geocoder geoCoder = new Geocoder(context, locale);
        List<Address> matches = null;
        try {
            double[]str = getLatLang(latLng);
            matches = geoCoder.getFromLocation(str[0], str[1], 1);

        } catch (Exception e) {
            Log.d("tag4me", e.getMessage());
            e.printStackTrace();
        }
        Address bestMatch = (matches == null ? null : matches.get(0));
        if(bestMatch!=null && bestMatch.getPostalCode()!=null && bestMatch.getLocality()!=null){
            //Log.d("tag4me", bestMatch.getLocality() +" "+ bestMatch.getPostalCode());
            return new String[]{bestMatch.getLocality(), bestMatch.getPostalCode()};
        }else{
            return null;
        }
    }

    static public String getFullAdress(String latLng, Context context){
        Locale locale = new Locale("ru", "RU");
        Geocoder geoCoder = new Geocoder(context, locale);
        List<Address> matches = null;
        try {
            double[]str = getLatLang(latLng);
            matches = geoCoder.getFromLocation(str[0], str[1], 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
        if(bestMatch!=null && bestMatch.getPostalCode()!=null && bestMatch.getLocality()!=null){
            //Log.d("tag4me", bestMatch.getLocality() +" "+ bestMatch.getPostalCode());
            return bestMatch.getAddressLine(0);
        }else{
            return "";
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        myPos = latLng;
        setPlace();
        updateData();
    }
}
