package com.example.carins.repo;

import com.example.carins.model.InsurancePolicyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePolicyLogRepository extends JpaRepository<InsurancePolicyLog, Long> {
    boolean existsByPolicyId(Long policyId);
}

