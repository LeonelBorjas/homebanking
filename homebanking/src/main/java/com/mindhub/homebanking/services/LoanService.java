package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoanService {
    ResponseEntity<?> getLoansAvailable(Authentication authentication);

    ResponseEntity<?> createLoanForAuthenticatedClient(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO);
}
