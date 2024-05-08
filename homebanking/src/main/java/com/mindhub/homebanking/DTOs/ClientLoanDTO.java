package com.mindhub.homebanking.DTOs;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.Set;

public class ClientLoanDTO {
    private Long id;

    private Long loanId;

    private String name;

    private int payments;

    private double amount;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayments();
        this.amount = clientLoan.getAmount();


    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
