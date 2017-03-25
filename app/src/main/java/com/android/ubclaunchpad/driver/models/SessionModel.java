package com.android.ubclaunchpad.driver.models;

import com.android.ubclaunchpad.driver.util.StringUtils;
import com.android.ubclaunchpad.driver.util.UserManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherryuan on 2017-02-04.
 */

public class SessionModel {

    // list of strings representing driver users' unique IDs
    private List<String> drivers;
    // list of strings representing passenger users' unique IDs
    private List<String> passengers;
    // location of the starting location
    private String location;
    private String name;


    public SessionModel(){
        drivers = new ArrayList<>();
        passengers = new ArrayList<>();
        location = "";
    }

    public SessionModel(LatLng latLng){
        drivers = new ArrayList<>();
        passengers = new ArrayList<>();
        this.location = StringUtils.latLngToString(latLng);
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }

    public void addDriver(String driver) {
        this.drivers.add(driver);
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(String passenger) {
        this.passengers.add(passenger);
    }

    //TODO passing an object to setter method causes getting SessionModel to crash
//    public void setLocation(LatLng latLng) {
//        this.location = StringUtils.latLngToString(latLng);
//    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDrivers() {
        return drivers;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public String getLocation() {
        return location;
    }

    public String getName() { return name; }

    // creates new Session with the creator in the drivers list if they're a driver
    // or passengers list if they're a passenger.
    // other than the creator lists are empty, and latlng should be calculated based on creator's location
    public static SessionModel createNewSession(String name, LatLng latLng) {

        SessionModel sessionModel = new SessionModel(latLng);
        sessionModel.setName(name);

        try {
            User user = UserManager.getInstance().getUser();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String uid = firebaseUser.getUid();

            if (user.isDriver()){
                sessionModel.addDriver(uid);
            } else {
                sessionModel.addPassenger(uid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionModel;
    }
}
