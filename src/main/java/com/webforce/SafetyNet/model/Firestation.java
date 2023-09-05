package com.webforce.SafetyNet.model;

public class Firestation {

    private String address;
    private String station;

    public Firestation() {
    }

    public Firestation(String adress, String station) {
        this.address = address;
        this.station = station;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
