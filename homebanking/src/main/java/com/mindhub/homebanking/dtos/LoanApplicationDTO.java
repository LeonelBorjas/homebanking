package com.mindhub.homebanking.dtos;

public record LoanApplicationDTO(Long id, String destinationAccount, double amount, int payments)  {
}
