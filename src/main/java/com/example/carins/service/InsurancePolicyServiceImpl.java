package com.example.carins.service;

import com.example.carins.exception.CarNotFound;
import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InsurancePolicyServiceImpl implements  InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    private final CarService carService;

    public InsurancePolicyServiceImpl(InsurancePolicyRepository policyRepository, CarService carService) {
        this.policyRepository = policyRepository;
        this.carService = carService;
    }

    public InsurancePolicyDto createInsurance(Long carId, String provider, LocalDate date, int amount) {
        Car car = carService.getCarOrThrow(carId);

        LocalDate endDate = date.plusMonths(amount);

        InsurancePolicy insurancePolicy = new InsurancePolicy(car, provider, date, endDate);
        InsurancePolicy result = policyRepository.save(insurancePolicy);
        return new InsurancePolicyDto(carId, result.getProvider(), result.getStartDate(), result.getEndDate());
    }
}