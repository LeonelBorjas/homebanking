package com.mindhub.homebanking.models;

import jakarta.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int payments;

    private double amount;

    @ManyToOne  //cada ClientLoan está asociado con un único Cliente
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne  //cada ClientLoan está asociado con un único prestamo
    @JoinColumn(name = "loan_id")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan(int payments, double amount) {
        this.payments = payments;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }
}
