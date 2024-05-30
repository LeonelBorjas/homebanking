package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.TransactionType;

public record TransactionsDTO (Double amount, String descriptions, String sourceAccountNumber, String targetAccountNumber) {
}
