package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

public interface AccountService {
    ResponseEntity<?> getAccount();

    ResponseEntity<?> getAccountsById(@PathVariable Long id, Authentication authentication);

    ResponseEntity<String> createAccount(Authentication authentication);
}
