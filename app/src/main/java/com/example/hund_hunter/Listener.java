package com.example.hund_hunter;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Listener extends AppCompatActivity implements GoogleMap.OnMarkerClickListener{


    @Override
    public boolean onMarkerClick(final Marker marker) {
        TextView text = findViewById(R.id.bottom_sheet_3);
        text.setText(marker.getPosition().toString());
        return true;
    }
}
