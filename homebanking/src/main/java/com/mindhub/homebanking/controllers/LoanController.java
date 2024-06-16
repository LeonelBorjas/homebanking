package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.requestBody.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
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

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;




    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<LoanDTO> availableLoans = loanService.getLoansDTO();
        return  ResponseEntity.ok(availableLoans);
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<?> createLoanForAuthenticatedClient(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.getClientByEmail(authentication.getName()); // obtengo el cliente
        String username = client.getEmail(); // obtengo el username del cliente

        // Valida que el monto de prestamo sea mayor a 0
        if (loanApplicationDTO.amount() <= 0) {
            return new ResponseEntity<>("Please, check amount entry", HttpStatus.FORBIDDEN);
        }

        // Valida que el monto de prestamo sea mayor a 0
        if (loanApplicationDTO.payments() <= 0) {
            return new ResponseEntity<>("Please, check the entries and try again", HttpStatus.FORBIDDEN);
        }

        // valida si NO existe un prestamo igual a la cuota.
        if (!loanService.existsById(loanApplicationDTO.id())){
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        // Valida si el amount no excede el maxAmount del loan solicitado
        Loan loan = loanService.findById(loanApplicationDTO.id()).get(); //Obtiene el préstamo solicitado.
        if (loanApplicationDTO.amount() > loan.getMaxAmount()) { //si el monto solicitado excede el monto máximo permitido por el préstamo.
            return new ResponseEntity<>("Loan amount exceeds maximum amount", HttpStatus.FORBIDDEN);
        }

        // Valida si las cuotas están dentro de las opciones del loan solicitado
        List<Integer> payments = loan.getPayments();
        if (!payments.contains(loanApplicationDTO.payments())) {
            return new ResponseEntity<>("The selected installments are not within the current options for this loan", HttpStatus.FORBIDDEN);
        }

        // Valida que la cuenta de destino exista
        Account destinationAccount = accountService.findByNumber(loanApplicationDTO.destinationAccount()); // Obtiene la cuenta de destino
        if (destinationAccount == null) { // Valida que la cuenta de destino exista
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        // Valida que la cuenta de destino no pertenezca al cliente autenticado
        if (!destinationAccount.getClient().getEmail().equals(username)) {
            return new ResponseEntity<>("Destination account does not belong to you.", HttpStatus.FORBIDDEN);
        }

        // Calcular la tasa de interés de acuerdo a las cuotas
        double interestRate;

        if (loanApplicationDTO.payments() < 12) { // Si las cuotas son menores a 12 15%
            interestRate = 0.15;
        } else if (loanApplicationDTO.payments() == 12) { // Si las cuotas son iguales a 12 20%
            interestRate = 0.20;
        } else {
            interestRate = 0.25; // Si las cuotas son mayores a 12 25%
        }

        // Crear y configurar el nuevo préstamo con el monto y la tasa de interés
        double amountPlusInterest = loanApplicationDTO.amount() + (loanApplicationDTO.amount() * interestRate); // Calcula el monto total del préstamo incluyendo los intereses.
        ClientLoan newClientLoan = new ClientLoan(loanApplicationDTO.payments(), amountPlusInterest);
        newClientLoan.setLoan(loan);
        newClientLoan.setClient(client);
        client.addClientLoan(newClientLoan);

        clientLoanRepository.save(newClientLoan);

        // Guardar entidades actualizadas
        clientService.saveClient(client);

        // Crear y guardar la transacción
        String description = "New loan approved and credited"; //Define la descripción de la transacción.
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.amount(), date, description);;
        transaction.setAccount(destinationAccount);
        transactionRepository.save(transaction);

        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.amount()); //Actualiza el saldo de la cuenta de destino sumándole el monto del préstamo.
        accountService.saveAccount(destinationAccount); //Guarda la cuenta de destino


        return new ResponseEntity<>("Loan created and credited to destination account", HttpStatus.CREATED);
    }

}
