package com.example.fujitsuv4;

import com.example.fujitsuv4.exceptions.DeliverySystemException;
import com.example.fujitsuv4.models.RegBaseFee;
import com.example.fujitsuv4.models.Weather;
import com.example.fujitsuv4.repositories.RegBaseFeeRepository;
import com.example.fujitsuv4.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import java.util.Arrays;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private RegBaseFeeRepository regBaseFeeRepository;

    /**
     * Method which calculates the delivery fee based on the town and vehicle (throws an Exception if business rules are ignored)
     * @param townName the town in which the delivery takes place
     * @param vehicle the delivery vehicle
     * @return double delivery fee
     */
    public double getDeliveryFee(String townName, String vehicle){
        List<String> validTowns = Arrays.asList("tallinn", "pärnu", "tartu", "parnu");
        List<String> validVehicles = Arrays.asList("car", "scooter", "bike");


        //Exception if the town or vehicle is invalid
        if(!validTowns.contains(townName.toLowerCase()) || !validVehicles.contains(vehicle.toLowerCase())){
            throw new DeliverySystemException("Invalid arguments for town/vehicle");
        }


        //Lower case in order to ease the process of comparison
        //Also, in case of pärnu, a change in order to make both "parnu" and "pärnu" valid inputs
        townName = townName.toLowerCase();
        vehicle = vehicle.toLowerCase();

        if(townName.equals("parnu")){
            townName = townName.replaceAll("a", "ä");
        }

        RegBaseFee RBFEntity = regBaseFeeRepository.regionalBaseFeeBasedOnCityAndVehicle(townName);

        Weather weather = weatherRepository.findLatestWeatherDataForCity(townName);

        double RBF = 0;
        double ATEF = 0;
        double WSEF = 0;
        double WPEF = 0;



        //A simple switch statement in order to get the right regional base fee of the vehicle.
        switch(vehicle){
            case "car":
                RBF = RBFEntity.getCar();
                break;
            case "scooter":
                RBF = RBFEntity.getScooter();
                break;
            case "bike":
                RBF = RBFEntity.getBike();
                break;
        }


        //If vehicle is a bike or a scooter, calculate extra fees for weather conditions
        if(vehicle.equals("bike") || vehicle.equals("scooter")){
            ATEF = calculateATEF(weather);
            WSEF = calculateWSEF(weather);
            WPEF = calculateWPEF(vehicle, weather);
        }

        return RBF + ATEF + WSEF + WPEF;
    }


    /**
     * Simple method to calculate weahter phenomenon extra fee based of the vehicle and weather phenomenon
     * Throws exception if the weather and vehicle combo is forbidden.
     * @param vehicle vehicle type
     * @param weather the current weather to get statistics from
     * @return double value, extra fee based on weather phenomenon
     */
    private double calculateWPEF(String vehicle, Weather weather) {
        double WPEF = 0;

        if(vehicle.equals("bike")){
            if(weather.getPhenomenon().toLowerCase().matches(".*(snow|sleet).*")){
                WPEF = 1;
            }else if(weather.getPhenomenon().toLowerCase().matches(".*rain.*")){
                WPEF = 0.5;
            }else if(weather.getPhenomenon().toLowerCase().matches(".*(glaze|hail|thunder).*")){
                throw new DeliverySystemException("Usage of selected vehicle type is forbidden");
            }
        }

        return WPEF;

    }

    /**
     * Method for calculating the extra fee based on wind speed
     * Throws Exception if the vehicle type is forbidden due to wind speed.
     * @param weather the current weather to get statistics from
     * @return double WSEF (Wind Speed Extra Fee)
     */
    private double calculateWSEF(Weather weather) {
        double WSEF = 0;
        if(weather.getWindSpeed() >= 10 && weather.getWindSpeed() <= 20){
            WSEF = 0.5;
        }else if(weather.getWindSpeed() > 20){
            throw new DeliverySystemException("Usage of selected vehicle type is forbidden");
        }

        return WSEF;


    }

    /**
     * Method to calculate the extra fee based of air temperature
     * @param weather the current weather which to get statistics from
     * @return double ATEF (air temperature extra fee)
     */
    private double calculateATEF(Weather weather) {
        double ATEF = 0;
        if(weather.getAirTemp() < -10){
            ATEF = 1;
        }else if(weather.getAirTemp() >= -10 && weather.getAirTemp() <= 0){
            ATEF = 0.5;
        }
        return  ATEF;
    }


}
