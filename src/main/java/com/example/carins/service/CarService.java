package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.web.dto.InsurancePolicyDto;

import java.time.LocalDate;
import java.util.List;

public interface CarService {

    List<Car> listCars();

    boolean isInsuranceValid(Long carId, LocalDate date);

    List<InsurancePolicyDto> getHistory(Long carId);

    Car getCarOrThrow(Long carId);
}
