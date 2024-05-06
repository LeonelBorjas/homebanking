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
    public ResponseEntity<?> getAccount(){
        List<Account> account = accountRepository.findAll();
        List<AccountDTO> accountDTOS = account.stream().map(accounts -> new AccountDTO(accounts)).collect(java.util.stream.Collectors.toList());
        if (!account.isEmpty()) {
            return new ResponseEntity<>(accountDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro la cuenta", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountsById(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null); // Buscar cuenta por ID
        if (account == null){ // Si no existe, devolver ResponseEntity con el codigo de estado personalizado
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso No encontrado");
        }

        AccountDTO accountDTO = new AccountDTO(account); // si existe devolver ResponseEntity con el accountDTO y el codigo de estado OK
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

}
