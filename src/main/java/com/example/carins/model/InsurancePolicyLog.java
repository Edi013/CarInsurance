package com.example.carins.model;

import jakarta.persistence.*;

@Entity
@Table(name = "insurance_policy_log")
public class InsurancePolicyLog {

    @Id
    @Column(name = "policy_id")
    private Long policyId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "policy_id")
    private InsurancePolicy policy;

    public InsurancePolicyLog() {}

    public InsurancePolicyLog(InsurancePolicy policy) {
        this.policy = policy;
    }
}
