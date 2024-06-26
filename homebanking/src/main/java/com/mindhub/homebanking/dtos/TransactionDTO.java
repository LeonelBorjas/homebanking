package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private TransactionType type;

    private double amount;

    private LocalDateTime date;

    private String description;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.description = transaction.getDescripton();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
