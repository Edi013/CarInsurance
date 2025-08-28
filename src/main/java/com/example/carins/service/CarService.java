package com.example.carins.service;

import com.example.carins.exception.CarNotFound;
import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        Car car = getCarOrThrow(carId);

        return policyRepository.existsActiveOnDate(carId, date);
    }

    public List<InsurancePolicyDto> getHistory(Long carId) {
        Car car = getCarOrThrow(carId);
        return policyRepository.findByCarId(carId).stream()
                .map(InsurancePolicyDto::tryFrom)
                .toList();

    }

    public Car getCarOrThrow(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFound("Car with id " + carId + " does not exist"));
    }
}
