package com.example.quakeviewer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuakeItem {
    private Date origin;
    private String location;
    private int depth;
    private String depthMeasurement;
    private Double latitude;
    private Double longitude;
    private Double magnitude;

    public Date getOrigin() { return this.origin; }
    public String getLocation() { return this.location; }
    public int getDepth() { return this.depth; }
    public String getDepthMeasurement() { return this.depthMeasurement; }
    public Double getLat() { return this.latitude; }
    public Double getLong() { return this.longitude; }
    public Double getMag() { return this.magnitude; }

    public QuakeItem(String data, String latitude, String longitude) {
        String[] fields = data.split(";");
        String[] depthInfo = getFieldValue(fields[3]).split(" ");

        try {
            this.origin = getOriginValue(fields[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.location = getLocationValue(fields[1]);
        this.depth = Integer.parseInt(depthInfo[0]);
        this.depthMeasurement = depthInfo[1];
        this.magnitude = Double.valueOf(getFieldValue(fields[4]));
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
    }

    private String getFieldValue(String field) {
        int valueIdx = field.indexOf(':');

        if (valueIdx == -1)
            return field;

        return field.substring(valueIdx + 1).trim();
    }

    private Date getOriginValue(String field) throws ParseException {
        String origin = getFieldValue(field);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        return formatter.parse(origin);
    }

    private String getLocationValue(String field) {
        String location = getFieldValue(field).toLowerCase();
        String[] locations = location.split(",");

        location = locations[0].substring(0, 1).toUpperCase() + locations[0].substring(1).toLowerCase();;

        for (int i = 1; i < locations.length; i++) {
            location += ", " + locations[i].substring(0, 1).toUpperCase() + locations[i].substring(1);
        }

        return location;
    }

    public Boolean IsMoreNorth(QuakeItem obj) {
        return this.latitude > obj.latitude;
    }

    public Boolean IsMoreEast(QuakeItem obj) {
        return this.longitude > obj.longitude;
    }

    public Boolean IsDeeper(QuakeItem obj) {
        return this.depth > obj.depth;
    }

    public Boolean IsNewer(QuakeItem obj) { return this.origin.after(obj.origin); }
}