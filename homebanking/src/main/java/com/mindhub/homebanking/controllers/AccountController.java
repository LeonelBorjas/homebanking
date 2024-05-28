package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

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

    @GetMapping("/accounts") // mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public ResponseEntity<?> getAccount(){
        List<Account> account = accountRepository.findAll();
        List<AccountDTO> accountDTOS = account
                .stream()
                .map(accounts -> new AccountDTO(accounts))
                .collect(Collectors.toList());
        // Convierte la lista de cuentas en un stream de elementos, Mapea cada acuenta a un objeto AccountDto y para crear una lista que contiene los elementos del stream.
        if (!account.isEmpty()) { // Verifica si la lista de cuentas no está vacía
            return new ResponseEntity<>(accountDTOS, HttpStatus.OK); // devuelve un ResponseEntity con la lista de AccountDTO y el estado HttpStatus.OK
        } else {
            return new ResponseEntity<>("No se encontro la cuenta", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccountsById(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null); // Buscar cuenta por ID
        if (account == null){ // Si no existe, devolver ResponseEntity con el codigo de estado personalizado
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso No encontrado");
        }

        AccountDTO accountDTO = new AccountDTO(account); // si existe devolver ResponseEntity con el accountDTO y el codigo de estado OK
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<String> createAccount(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("You reach the maximum limit of 3 accounts per client",HttpStatus.FORBIDDEN);
        }
        String number;
        do {
            number = "VIN-" + eightDigits();
        } while (accountRepository.findByNumber(number) != null);

        Account account = new Account(number, LocalDate.now(), 0.0);
        account.setClient(client);
        client.addAccount(account);

        clientRepository.save(client);
        accountRepository.save(account);


        return new ResponseEntity<>("Client account created", HttpStatus.CREATED);
    }

    public static String eightDigits() {
        Random random = new Random();
        int number = random.nextInt(100000000); // Genera un número entre 0 y 99999999
        return String.format("%08d", number);  // Completa con ceros a la izquierda si es necesario
    }

}
