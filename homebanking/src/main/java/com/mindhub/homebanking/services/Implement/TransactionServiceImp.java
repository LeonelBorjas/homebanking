package com.mindhub.homebanking.services.Implement;

import com.mindhub.homebanking.dtos.TransactionsDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImp implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
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

        Account sourceAccount = accountRepository.findByNumber(transactionsDTO.sourceAccountNumber());
        Account targetAccount = accountRepository.findByNumber(transactionsDTO.targetAccountNumber());


        if(sourceAccount == null){
            return new ResponseEntity<>("Source account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(targetAccount == null){
            return new ResponseEntity<>("Target account does not exists.", HttpStatus.FORBIDDEN);
        }

        if(!sourceAccount.getClient().getEmail().equals(authentication.getName())){
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


        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return new ResponseEntity<>("Transfer successful",HttpStatus.OK);
    }

}
