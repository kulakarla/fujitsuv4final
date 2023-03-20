package com.example.fujitsuv4.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.DataAmount;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Weather {

    @Id
    @GeneratedValue
    private int id;
    private String stationName;
    private int stationWMO;
    private double airTemp;
    private double windSpeed;
    private String phenomenon;

    private LocalDateTime obsTime;

    public Weather(int id, String stationName, int stationWMO, double airTemp, double windSpeed, String phenomenon, LocalDateTime obsTime) {
        this.id = id;
        this.stationName = stationName;
        this.stationWMO = stationWMO;
        this.airTemp = airTemp;
        this.windSpeed = windSpeed;
        this.phenomenon = phenomenon;
        this.obsTime = obsTime;
    }

    public Weather() {

    }

    public double getAirTemp() {
        return airTemp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public LocalDateTime getObsTime() {
        return obsTime;
    }
}
