package com.mindhub.homebanking.dtos.requestBody;

public record TransactionsDTO (Double amount, String descriptions, String sourceAccountNumber, String targetAccountNumber) {
}
