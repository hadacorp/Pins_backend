package com.hada.pins_backend.pin.service;

/**
 * Created by bangjinhyuk on 2021/12/01.
 * Modified by bangjinhyuk on 2022/03/19.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GpsToAddress {

    String googleKey;

    public GpsToAddress(String googleKey) {
        this.googleKey = googleKey;
    }

    private String getApiAddress(double latitude, double longitude) {
        String apiURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&language=ko&key="+googleKey;
        return apiURL;
    }

    private String getJSONData(String apiURL) throws Exception {
        String jsonString = new String();
        String buf;
        URL url = new URL(apiURL);
        URLConnection conn = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        while ((buf = br.readLine()) != null) {
            jsonString += buf;
        }
        return jsonString;
    }

    private String getRegionAddress(String jsonString) {
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONArray jArray = (JSONArray) jObj.get("results");
        jObj = (JSONObject) jArray.get(0);
        return (String) jObj.get("formatted_address");
    }

    public String getAddress(double latitude, double longitude) {
        String address;
        try{
            address = getRegionAddress(getJSONData(getApiAddress(latitude, longitude)));
        }catch (Exception e){
            address = "";
        }
        return address;
    }
}
