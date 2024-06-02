package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionService {
    ResponseEntity<?> transferMoney(@RequestBody TransactionsDTO transactionsDTO, Authentication authentication);
}
