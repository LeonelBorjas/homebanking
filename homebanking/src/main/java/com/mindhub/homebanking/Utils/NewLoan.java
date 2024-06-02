package com.mindhub.homebanking.Utils;

import jakarta.persistence.ElementCollection;

import java.util.List;

public class NewLoan {

    private String name;

    private double maxAmount,interest_rate;

    private List<Integer> payments;

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterest_rate() {
        return interest_rate;
    }
}
