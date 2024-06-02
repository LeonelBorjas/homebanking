package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClientService {
    ResponseEntity<?> getClients();

    ResponseEntity<?> getClientsById(@PathVariable Long id);
}
