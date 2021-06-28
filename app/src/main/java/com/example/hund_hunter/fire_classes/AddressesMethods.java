package com.example.hund_hunter.fire_classes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressesMethods {


    static public Address getAddress(String latLng, Context context, String lang, String country){
        Locale locale = new Locale(lang, country);
        Geocoder geoCoder = new Geocoder(context, locale);
        List<Address> matches = null;
        try {
            double[]str = getLatLang(latLng);
            matches = geoCoder.getFromLocation(str[0], str[1], 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Address bestMatch = (matches == null ? null : matches.get(0));
        if(bestMatch!=null && bestMatch.getPostalCode()!=null && bestMatch.getLocality()!=null){
            return bestMatch;
        }else{
            return null;
        }
    }

    static public String[] getLocalityAndPostal(String latLng, Context context){
        Address bestMatch = getAddress(latLng, context, "en", "US");
        if(bestMatch!=null && bestMatch.getPostalCode()!=null && bestMatch.getLocality()!=null){
            return new String[]{bestMatch.getLocality(), bestMatch.getPostalCode()};
        }else{
            return null;
        }
    }


    static public String getFullAddressLine(String latLng, Context context){
        Address bestMatch = getAddress(latLng, context, "ru", "RU");
        if(bestMatch!=null && bestMatch.getPostalCode()!=null && bestMatch.getLocality()!=null){
            return bestMatch.getAddressLine(0);
        }else{
            return "";
        }
    }

    public static double[] getLatLang(String string){
        //даже для меня темный лес
        String from_lat_lng = "";
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(string);
        while(m.find()) {
            from_lat_lng = m.group(1) ;
        }
        assert from_lat_lng != null;
        String[] gpsVal = from_lat_lng.split(",");
        return new double[]{Double.parseDouble(gpsVal[0]), Double.parseDouble(gpsVal[1])};
    }

}
