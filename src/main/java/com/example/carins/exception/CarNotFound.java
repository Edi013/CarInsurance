package com.example.carins.exception;

public class CarNotFound extends RuntimeException {
    public CarNotFound(Long carId) {
        super("Car with id " + carId + " does not exist");
    }

    public CarNotFound(String message) {
        super(message);
    }
}
