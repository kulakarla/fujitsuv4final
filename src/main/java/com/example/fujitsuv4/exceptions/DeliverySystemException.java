package com.example.fujitsuv4.exceptions;

/**
 * A simple exception for issues in connection to the Delivery Fee Calculation System
 */
public class DeliverySystemException extends RuntimeException {

    private final String message;

    public DeliverySystemException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
