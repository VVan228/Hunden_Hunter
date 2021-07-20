
package com.example.hund_hunter.main_activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.hund_hunter.R;
import com.example.hund_hunter.data_classes.Order;
import com.example.hund_hunter.data_classes.User;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SeekerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    FireDB db;
    FireDB users;
    LatLng myPos;
    HashMap<String, Marker> markers;
    HashMap<String, Bitmap> image_cache;
    Set<String> loading_images;
    Set<String> loaded_indexes;
    String curPostal;
    Marker lastMarker;
    static String currentImagePath = "";
    public static final float MY_COLOR = 10.0f;

    SharedPreferences settings;
    public static final String TUTORIAL = "tutor";
    public static final String APP_PREFERENCES = "settings";


    Polygon currentPolygon;
    final double marginRadius = 0.0005;
    LatLng polygonCenter;
    PointsHolder polygonPoints;;

    boolean isFABOpen;
    FloatingActionButton fab;
    LinearLayout fab1;
    LinearLayout fab2;
    LinearLayout fab3;
    TextView fab1text;
    TextView fab2text;
    TextView fab3text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_activity);

        polygonPoints = new PointsHolder();
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        loading_images = new HashSet<>();
        loaded_indexes = new HashSet<>();
        image_cache = new HashMap<>();
        markers = new HashMap<>();
        users = new FireDB(new String[]{"users"});
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


        myPos = ChoiceActivity.myPlace;
        polygonCenter = myPos;
        if(myPos == null){
            Toast.makeText(SeekerActivity.this, "не получилось определить локацию", Toast.LENGTH_LONG).show();
            return;
        }

        polygonPoints.add(new LatLng(myPos.latitude + marginRadius,myPos.longitude));
        polygonPoints.add(new LatLng(myPos.latitude,myPos.longitude - marginRadius));
        polygonPoints.add(new LatLng(myPos.latitude - marginRadius,myPos.longitude));
        polygonPoints.add(new LatLng(myPos.latitude,myPos.longitude + marginRadius));



        if(!settings.contains(TUTORIAL)){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(TUTORIAL, "");
            editor.apply();

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.tutorial_dialog);
            dialog.setTitle("");

            Button cancel = (Button) dialog.findViewById(R.id.tutorial_cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }

        //записать текущую позицию и обновить бд
        setPlaceAndUpdateData();
        //получение меток


        fab = (FloatingActionButton) findViewById(R.id.main_fab_menu);
        fab1 = findViewById(R.id.fab_menu_1);
        fab2 = findViewById(R.id.fab_menu_2);
        fab3 = findViewById(R.id.fab_menu_3);
        fab1text = findViewById(R.id.menu_text1);
        fab2text = findViewById(R.id.menu_text2);
        fab3text = findViewById(R.id.menu_text3);
        isFABOpen = false;
        fab.setOnClickListener(view -> {
            if(!isFABOpen){
                fab.setRotation(180);
                showFABMenu();
            }else{
                fab.setRotation(0);
                closeFABMenu();
            }
        });
        fab1.setOnClickListener(v -> {
            Intent intent = new Intent(SeekerActivity.this, UserAccountActivity.class);
            startActivity(intent);
        });
        fab2.setOnClickListener(v -> {
            Intent intent = new Intent(SeekerActivity.this, ListOfMyItems.class);
            startActivity(intent);
        });
        fab3.setOnClickListener(v -> {
            Intent intent = new Intent(SeekerActivity.this, OrderCreationActivity.class);
            startActivity(intent);
        });
    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(getResources().getDimension(R.dimen.standard_55)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fab1text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fab2.animate().translationY(getResources().getDimension(R.dimen.standard_105)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fab2text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fab3.animate().translationY(getResources().getDimension(R.dimen.standard_155)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fab3text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fab2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fab3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fab1text.setVisibility(View.GONE);
        fab2text.setVisibility(View.GONE);
        fab3text.setVisibility(View.GONE);
    }


    void setPlaceAndUpdateData(){
        //Log.d("tag4me", "setPlace start");
        String[]address = AddressesMethods.getLocalityAndPostal(myPos.toString(), this);
        if(address==null){
            Toast.makeText(SeekerActivity.this, "не получилось распознать адрес", Toast.LENGTH_LONG).show();
            return;
        }
        String locality = address[0];
        String postal = address[1];
        Log.d("tag4me", locality+" "+postal);
        for(String s: loaded_indexes){
            Log.d("tag4me", s);
        }
        if(!loaded_indexes.contains(locality+postal)){
            db = new FireDB(new String[]{"orders", locality, postal});
            updateData();
            //Log.d("tag4me", "added");
            loaded_indexes.add(locality+postal);
        }

    }


    void updateData(){
        if(db == null){// || loaded_indexes.contains(curPostal)){
            return;
        }
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

            double[] res = AddressesMethods.getLatLang(cords);
            polygonPoints.addThreeWithMargin(new LatLng(res[0], res[1]));
            polygonPoints.add(new LatLng(res[0], res[1]));
            updatePolygon();


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
            Log.d("tag4me1", "removed " + order.getCoord());
            Objects.requireNonNull(markers.get(order.getCoord())).remove();
            markers.remove(order.getCoord());

        }).create());
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        currentPolygon = mMap.addPolygon(polygonPoints.getOptions());

        //слушатель нажатий на маркеры
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(marker -> {
            final ProgressBar image_progressBar = findViewById(R.id.bottom_image_progressBar);


            if(lastMarker!=null){
                lastMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_marker_unselected));
            }
            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_marker_selected));
            lastMarker = marker;
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

                final TextView no_photo = findViewById(R.id.no_foto);
                String path = markerData.getString("photo");
                currentImagePath = path;
                if(path.equals("")){
                    photo.setVisibility(View.GONE);
                    image_progressBar.setVisibility(View.GONE);
                    no_photo.setVisibility(View.VISIBLE);
                }else{
                    if(image_cache.containsKey(path)) {
                        no_photo.setVisibility(View.GONE);
                        image_progressBar.setVisibility(View.GONE);

                        photo.setImageBitmap(image_cache.get(path));
                        photo.setVisibility(View.VISIBLE);
                    }else {
                        no_photo.setVisibility(View.GONE);
                        photo.setVisibility(View.GONE);
                        image_progressBar.setVisibility(View.VISIBLE);

                        if(loading_images.contains(path)){
                            return true;
                        }

                        loading_images.add(path);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imageRef = storageRef.child(markerData.getString("photo"));
                        final long ONE_MEGABYTE = 1024 * 1024 * 5;
                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Data for "images/island.jpg" is returns, use this as needed
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                if(currentImagePath.equals(path)){
                                    photo.setImageBitmap(bitmap);
                                    image_progressBar.setVisibility(View.GONE);
                                    photo.setVisibility(View.VISIBLE);
                                }
                                loading_images.remove(path);
                                image_cache.put(path, bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                image_progressBar.setVisibility(View.GONE);
                                no_photo.setVisibility(View.VISIBLE);
                                Toast.makeText(SeekerActivity.this, "картинку не скачал.. " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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
                //.icon(BitmapDescriptorFactory.defaultMarker(MY_COLOR)));
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker_unselected)));
        a.setTag(tag);

        markers.put(new LatLng(lat,lon).toString(), a);
    }



    @Override
    public void onMapClick(LatLng latLng) {
        if(isFABOpen){
            fab.setRotation(0);
            closeFABMenu();
            return;
        }

        polygonPoints.addThreeWithMargin(latLng);
        updatePolygon();

        myPos = latLng;
        setPlaceAndUpdateData();
    }



    static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public double distance (LatLng p1, LatLng p2){
        double lat_a = p1.latitude;
        double lat_b = p2.latitude;
        double lng_a = p1.longitude;
        double lng_b = p2.longitude;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }

    double angle(LatLng p1, LatLng p2) {
        double lat1 = p1.latitude;
        double lat2 = p2.latitude;
        double long1 = p1.longitude;
        double long2 = p2.longitude;


        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        //brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }

    double angleFromCenter(LatLng o){
        return angle(o, polygonCenter);
    }
    double distanceFromCenter(LatLng o){
        return distance(o, polygonCenter);
    }
    LatLng rotatePoint(LatLng point, LatLng midPoint, double angle){
        double piDiv180 = Math.PI / 180;

        // Convert the input angle to radians
        final double r = angle * piDiv180;

        // Create local variables using appropriate nomenclature
        final double x = point.longitude;
        final double y = point.latitude;
        final double mx = midPoint.longitude;
        final double my = midPoint.latitude;

        // Offset input point by the midpoint so the midpoint becomes the origin
        final double ox = x - mx;
        final double oy = y - my;

        // Cache trig results because trig is expensive
        final double cosr = Math.cos(r);
        final double sinr = Math.sin(r);

        // Perform rotation
        final double dx = ox * cosr - oy * sinr;
        final double dy = ox * sinr + oy * cosr;

        // Undo the offset
        return new LatLng(dy + my, dx + mx);
    }


    void updatePolygon(){
        currentPolygon.remove();
        currentPolygon = mMap.addPolygon(polygonPoints.getOptions());
    }

    class PointsHolder{
        ArrayList<LatLng> points;
        PointsComparator comparator;

        PointsHolder(){
            points = new ArrayList<>();
            comparator = new PointsComparator();
        }

        void add(LatLng point){
            points.add(point);
            Collections.sort(points, new PointsComparator());

            checkCircle();
        }
        void addThreeWithMargin(LatLng point){
            LatLng elevatedPoint = new LatLng(point.latitude+marginRadius*3, point.longitude);
            LatLng left = rotatePoint(elevatedPoint, point, angleFromCenter(elevatedPoint)-45);
            LatLng right = rotatePoint(elevatedPoint, point, angleFromCenter(elevatedPoint)+45);
            //LatLng center = rotatePoint(elevatedPoint, point, angleFromCenter(elevatedPoint));
            points.add(left);
            points.add(right);
            //points.add(center);

            polygonCenter = calculateCenter();
            Collections.sort(points, new PointsComparator());
            checkCircle();
        }

        void checkCircle(){
            for(int i = 1; i<points.size(); i++){
                double dist = Math.min( angleFromCenter(points.get(i)), angleFromCenter(points.get(i-1) ));
                Log.d("bbbb", Double.toString(dist));
                if(Math.abs( angleFromCenter(points.get(i)) - angleFromCenter(points.get(i-1))) < 10 &&
                        Math.abs( distanceFromCenter(points.get(i)) - distanceFromCenter(points.get(i-1))) > 10){
                    if(distanceFromCenter(points.get(i)) > distanceFromCenter(points.get(i-1))){
                        points.remove(i-1);
                    }else{
                        points.remove(i);
                    }
                }
            }
        }

        LatLng calculateCenter(){
            double lat = 0.0;
            double lng = 0.0;
            for(LatLng p: points){
                lat += p.latitude;
                lng += p.longitude;
            }
            return new LatLng(lat/points.size(), lng/points.size());
        }

        PolygonOptions getOptions(){
            PolygonOptions res = new PolygonOptions()
                    .fillColor(Color.argb(20,0,0,0))
                    .strokeColor(Color.argb(255,255,255,255))
                    .strokeWidth(5);
            for (LatLng p: points) {
                res.add(p);
            }
            return res;
        }
    }

    class PointsComparator implements Comparator<LatLng>{

        @Override
        public int compare(LatLng o1, LatLng o2) {
            return Double.compare(angleFromCenter(o1), angleFromCenter(o2));
        }
    }
}
