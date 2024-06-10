package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.requestBody.TransactionsDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/transactions")
    @Transactional //Se utiliza para administrar procesos transaccionales en aplicaciones java y cumplan con los metodos ACID (ATOMICIDAD)
    public ResponseEntity<?> transferMoney(@RequestBody TransactionsDTO transactionsDTO, Authentication authentication){

        if(transactionsDTO.descriptions().isBlank()){
            return new ResponseEntity<>("Description cannot be blank", HttpStatus.FORBIDDEN);
        }

        if(transactionsDTO.sourceAccountNumber().isBlank() || transactionsDTO.targetAccountNumber().isBlank()){
            return new ResponseEntity<>("Source Account or Target Account cannot be blank.",HttpStatus.FORBIDDEN);
        }

        if(transactionsDTO.amount() == null || transactionsDTO.amount() <= 0){
            return new ResponseEntity<>("Amount cannot be blank or cero", HttpStatus.FORBIDDEN);
        }

        Account sourceAccount = accountService.findByNumber(transactionsDTO.sourceAccountNumber());
        Account targetAccount = accountService.findByNumber(transactionsDTO.targetAccountNumber());


        if(sourceAccount == null){
            return new ResponseEntity<>("Source account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(targetAccount == null){
            return new ResponseEntity<>("Target account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(!sourceAccount.getClient().getEmail().equals(authentication.getName())){ //Valida que el cliente autenticado sea el propietario de la cuenta
            return new ResponseEntity<>("Source account does not belong to an authenticated client.", HttpStatus.FORBIDDEN);
        }

        if(sourceAccount.getBalance() < transactionsDTO.amount()){
            return new ResponseEntity<>("Insufficient funds in the account",HttpStatus.BAD_REQUEST);
        }

        if(sourceAccount.equals(targetAccount)){
            return new ResponseEntity<>("Source and Target account cannot be the same", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-transactionsDTO.amount(), LocalDateTime.now(),transactionsDTO.descriptions());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,transactionsDTO.amount(),LocalDateTime.now(),transactionsDTO.descriptions());

        debitTransaction.setAccount(sourceAccount);
        creditTransaction.setAccount(targetAccount);

        sourceAccount.setBalance(sourceAccount.getBalance()- transactionsDTO.amount());
        targetAccount.setBalance(targetAccount.getBalance()+ transactionsDTO.amount());


        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);
        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(targetAccount);

        return new ResponseEntity<>("Transfer successful",HttpStatus.OK);
    }

}


