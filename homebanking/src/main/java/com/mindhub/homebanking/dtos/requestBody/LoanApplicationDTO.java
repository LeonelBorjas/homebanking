package com.mindhub.homebanking.dtos.requestBody;

public record LoanApplicationDTO(Long id, String destinationAccount, double amount, int payments)  {
}
