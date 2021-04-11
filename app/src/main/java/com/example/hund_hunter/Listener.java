package com.example.hund_hunter;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class Listener extends AppCompatActivity implements GoogleMap.OnMarkerClickListener{


    @Override
    public boolean onMarkerClick(Marker marker) {
        TextView text = findViewById(R.id.bottom_sheet);
        text.setText(marker.getPosition().toString());
        return true;
    }
}
