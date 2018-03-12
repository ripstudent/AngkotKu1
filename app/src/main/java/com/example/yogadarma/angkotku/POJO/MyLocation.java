package com.example.yogadarma.angkotku.POJO;

/**
 * Created by yoga darma on 3/12/2018.
 */

public class MyLocation {

    public double latitude;
    public double longitude;
    public boolean login;

    public MyLocation(){

    }

    public MyLocation(double latitude, double longitude, boolean login){
        this.latitude = latitude;
        this.longitude = longitude;
        this.login = login;
    }

}
