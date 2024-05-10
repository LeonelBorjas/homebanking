package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;

    private String number;

    private LocalDate creationDate;

    private double balance;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        balance = account.getBalance();

        this.transactions = account.getTransactions().stream() // acedo a los metodos superiores
                .map(TransactionDTO::new) // devolvue el set del mismo tamaño
                .collect(Collectors.toSet());

    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }
}
