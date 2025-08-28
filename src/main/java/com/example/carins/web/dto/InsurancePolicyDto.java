package com.example.carins.web.dto;

import com.example.carins.model.InsurancePolicy;
import org.apache.logging.log4j.util.InternalException;

import java.time.LocalDate;

public record InsurancePolicyDto(Long carId, String provider, LocalDate startDate, LocalDate endDate){
    public static InsurancePolicyDto tryFrom(InsurancePolicy policy) {
        if (policy == null) throw new InternalException("Cannot convert to dto a InsurancePolicy that is null.");
        return new InsurancePolicyDto(
                policy.getCar() != null ? policy.getCar().getId() : null,
                policy.getProvider(),
                policy.getStartDate(),
                policy.getEndDate()
        );
    }
}

