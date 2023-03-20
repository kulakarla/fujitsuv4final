package com.example.fujitsuv4;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.server.ExportException;

@RestController
@RequestMapping(path = "api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService){
        this.deliveryService = deliveryService;
    }


    /**
     * A GET-method to calculate the delivery base fee depending on the town and vehicle
     * @param townName url parameter for the town name
     * @param vehicle url parameter for the type of vehicle
     * @return String type delivery fee or an error message.
     */
    @GetMapping(path = "/{townName}/{vehicle}")
    public String getDeliveryFee(@PathVariable String townName, @PathVariable String vehicle){
        try {
            return String.valueOf(deliveryService.getDeliveryFee(townName, vehicle));
        }catch(Exception e){
            return e.getMessage();
        }
    }


}
