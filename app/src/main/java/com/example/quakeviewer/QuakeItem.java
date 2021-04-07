package com.example.quakeviewer;

public class QuakeItem {
    private String origin;
    private String location;
    private int depth;
    private String depthMeasurement;
    private Double latitude;
    private Double longitude;
    private Double magnitude;

    public String getOrigin() { return this.origin; }
    public String getLocation() { return this.location; }
    public int getDepth() { return this.depth; }
    public String getDepthMeasurement() { return this.depthMeasurement; }
    public Double getLat() { return this.latitude; }
    public Double getLong() { return this.longitude; }
    public Double getMag() { return this.magnitude; }

    public QuakeItem(String data, String latitude, String longitude) {
        String[] fields = data.split(";");
        String[] depthInfo = getFieldValue(fields[3]).split(" ");

        this.origin = getFieldValue(fields[0]);
        this.location = getFieldValue(fields[1]);
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

    public Boolean IsMoreNorth(QuakeItem obj) {
        return this.latitude > obj.latitude;
    }

    public Boolean IsMoreEast(QuakeItem obj) {
        return this.longitude > obj.longitude;
    }

    public Boolean IsDeeper(QuakeItem obj) {
        return this.depth > obj.depth;
    }
}