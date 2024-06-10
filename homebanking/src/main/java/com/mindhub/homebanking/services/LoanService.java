package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    List<Loan> getLoans();

    List<LoanDTO> getLoansDTO();

    boolean existsById(Long id);

    Optional<Loan> findById(Long id);

}
