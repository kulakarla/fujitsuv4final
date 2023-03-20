package com.example.fujitsuv4.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class RegBaseFee {

    @Id
    @GeneratedValue
    private int id;

    private String stationName;

    private double car;

    private double scooter;

    private double bike;

    public double getCar() {
        return car;
    }

    public double getScooter() {
        return scooter;
    }

    public double getBike() {
        return bike;
    }
}
