package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;



    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<LoanDTO> availableLoans = loanService.getLoansDTO();
        return  ResponseEntity.ok(availableLoans);
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<?> createLoanForAuthenticatedClient(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.getClientByEmail(authentication.getName());
        String username = client.getEmail();

        // Valida que el monto de prestamo sea positivo
        if (loanApplicationDTO.amount() <= 0) {
            return new ResponseEntity<>("Please, check amount entry", HttpStatus.FORBIDDEN);
        }

        // Valida que el monto de prestamo sea
        if (loanApplicationDTO.payments() <= 0) {
            return new ResponseEntity<>("Please, check the entries and try again", HttpStatus.FORBIDDEN);
        }

        // valida si NO existe un prestamo igual a la cuota.
        if (!loanService.existsById(loanApplicationDTO.id())){
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        // Valida si el amount no excede el maxAmount del loan solicitado
        Loan loan = loanService.findById(loanApplicationDTO.id()).get();
        if (loanApplicationDTO.amount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Loan amount exceeds maximum amount", HttpStatus.FORBIDDEN);
        }

        // Valida si las cuotas están dentro de las opciones del loan solicitado
        List<Integer> payments = loan.getPayments();
        if (!payments.contains(loanApplicationDTO.payments())) {
            return new ResponseEntity<>("The selected installments are not within the current options for this loan", HttpStatus.FORBIDDEN);
        }

        // Valida que la cuenta de destino exista
        Account destinationAccount = accountService.findByNumber(loanApplicationDTO.destinationAccount());
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        // Valida que la cuenta de destino pertenezca al cliente autenticado
        if (!destinationAccount.getClient().getEmail().equals(username)) {
            return new ResponseEntity<>("Destination account does not belong to you.", HttpStatus.FORBIDDEN);
        }

        // Calcular la tasa de interés de acuerdo a las cuotas
        double interestRate;

        if (loanApplicationDTO.payments() < 12) {
            interestRate = 0.15;
        } else if (loanApplicationDTO.payments() == 12) {
            interestRate = 0.20;
        } else {
            interestRate = 0.25;
        }

        // Crear y configurar el nuevo préstamo con el monto y la tasa de interés
        double amountPlusInterest = loanApplicationDTO.amount() + (loanApplicationDTO.amount() * interestRate);
        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.payments(), amountPlusInterest);
        //newClientLoan.setClient(client);
        client.addClientLoan(newClientLoan);

        // Guardar entidades actualizadas
        clientService.saveClient(client);

        // Crear y guardar la transacción
        String description = "New loan approved and credited";
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.amount(), date, description);;
        transaction.setAccount(destinationAccount);
        transactionRepository.save(transaction);

        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.amount());
        accountService.saveAccount(destinationAccount);

        return new ResponseEntity<>("Loan created and credited to destination account", HttpStatus.CREATED);
    }

}
