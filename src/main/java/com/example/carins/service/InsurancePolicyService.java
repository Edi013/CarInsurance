package com.example.carins.service;

import com.example.carins.web.dto.InsurancePolicyDto;

import java.time.LocalDate;

public interface InsurancePolicyService {
    InsurancePolicyDto createInsurance(Long carId, String provider, LocalDate date, int amount);
}
