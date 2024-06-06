package com.example.followvehicle.model;

public class Location {
    private double lat;
    private double lng;

    private String datetime;

    public Location(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public String getDatetime() {
        return datetime;
    }

    public Location(String data_str){

        data_str = data_str.replace("[","").replace("]","");
        this.lat = Double.parseDouble(data_str.split(",")[0]);
        this.lng = Double.parseDouble(data_str.split(",")[1]);
        this.datetime = data_str.split(",")[2];

    }
}
