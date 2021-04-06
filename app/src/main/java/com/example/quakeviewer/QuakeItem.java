package com.example.quakeviewer;

public class QuakeItem {
    private String origin;
    private String location;
    private String depth;
    private Double latitude;
    private Double longitude;
    private Double magnitude;

    public String getOrigin() { return this.origin; }
    public String getLocation() { return this.location; }
    public String getDepth() { return this.depth; }
    public Double getLat() { return this.latitude; }
    public Double getLong() { return this.longitude; }
    public Double getMag() { return this.magnitude; }

    public QuakeItem(String data, Double latitude, Double longitude) {
        String[] fields = data.split(";");
        this.origin = getFieldValue(fields[0]);
        this.location = getFieldValue(fields[1]);
        this.depth = getFieldValue(fields[3]);
        this.magnitude = Double.valueOf(getFieldValue(fields[4]));
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String getFieldValue(String field) {
        int valueIdx = field.indexOf(':');

        if (valueIdx == -1)
            return field;

        return field.substring(valueIdx).trim();
    }
}
