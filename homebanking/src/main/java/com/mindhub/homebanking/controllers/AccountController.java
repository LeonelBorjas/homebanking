package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOs.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAccount(){
    List<AccountDTO> accounts = accountRepository.findAll().stream().map(AccountDTO::new).collect(toList()); // Busca todas las cuentas

        if (!accounts.isEmpty()) {
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountsById(@PathVariable Long Id){
        Account account = accountRepository.findById(Id).orElse(null); // Buscar cuenta por ID
        if (account == null){ // Si no existe, devolver ResponseEntity con el codigo de estado personalizado
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AccountDTO accountDTO = new AccountDTO(account); // si existe devolver ResponseEntity con el accountDTO y el codigo de estado OK
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

}
