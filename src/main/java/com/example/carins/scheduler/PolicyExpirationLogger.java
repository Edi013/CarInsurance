package com.example.carins.scheduler;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.model.InsurancePolicyLog;
import com.example.carins.repo.InsurancePolicyLogRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PolicyExpirationLogger {

    private static final Logger log = LoggerFactory.getLogger(PolicyExpirationLogger.class);
    private final InsurancePolicyRepository policyRepository;
    private final InsurancePolicyLogRepository policyLogRepository;

    public PolicyExpirationLogger(InsurancePolicyRepository policyRepository,
                                  InsurancePolicyLogRepository policyLogRepository) {
        this.policyRepository = policyRepository;
        this.policyLogRepository = policyLogRepository;
    }

    @Scheduled(fixedRate = 180000) // 30 * 60 * 1000 = 30 min
    @Transactional
    public void logPoliciesExpiringWithinHour() {
        LocalDateTime now = LocalDateTime.now();

        List<InsurancePolicy> expiredPolicies =
                policyRepository.findAllByEndDateBefore(now.toLocalDate());

        for (InsurancePolicy policy : expiredPolicies) {
            if (policyLogRepository.existsByPolicyId(policy.getId())) {
                continue;
            }

            log.info("Policy {} for car {} - owner {} has expired on {}", policy.getId(), policy.getCar().getId(), policy.getCar().getOwner().getName(), policy.getEndDate());

            InsurancePolicyLog logEntry = new InsurancePolicyLog(policy);
            policyLogRepository.save(logEntry);
        }
    }
}
