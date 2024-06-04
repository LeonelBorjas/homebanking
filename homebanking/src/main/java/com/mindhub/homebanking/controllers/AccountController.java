package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController // Marca la clase como un controlador REST, lo que significa que responderá a las solicitudes HTTP
@RequestMapping("/api") // estamos asociando las peticiones a esta ruta
@CrossOrigin( origins = "*" )
public class AccountController {

    @Autowired //Cablecito para poder usar el repository
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts") // mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public ResponseEntity<?> getAccount(){
        return accountService.getAccount();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccountsById(@PathVariable Long id, Authentication authentication) {
        return accountService.getAccountsById(id, authentication);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication){
        return accountService.createAccount(authentication);
    }

}
