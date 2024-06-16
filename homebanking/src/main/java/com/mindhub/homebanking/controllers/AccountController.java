package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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

import static com.mindhub.homebanking.services.Implement.AccountServiceImp.eightDigits;

@RestController // Marca la clase como un controlador REST, lo que significa que responderá a las solicitudes HTTP
@RequestMapping("/api/clients") // estamos asociando las peticiones a esta ruta
@CrossOrigin( origins = "*" )
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService  clientService;

    @GetMapping("/accounts") // mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    //servlet para obtener todas las cuentas de un usuario autenticado
    @GetMapping("/current/accounts") // mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public List<AccountDTO> getAuthAccounts(Authentication authentication){
        Client client = clientService.getAuthClient(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){

        Client client = clientService.getAuthClient(authentication.getName());

        Account account = accountService.findByClientAndId(client, id);

        if (account != null) {
            AccountDTO accountDTO = accountService.getAccountById(id);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/current/account")
    public ResponseEntity<String> createAccount(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());

        if(client.getAccounts().size()>=3){
            return new ResponseEntity<>("You reach the maximum limit of 3 accounts per client",HttpStatus.FORBIDDEN);
        }
        String number;
        do {
            number = "VIN-" + eightDigits();
        } while (accountService.findByNumber(number) != null);

        Account account = new Account(number, LocalDate.now(), 0.0);
        account.setClient(client);
        client.addAccount(account);

        clientService.saveClient(client);
        accountService.saveAccount(account);


        return new ResponseEntity<>("Client account created", HttpStatus.CREATED);

}
}
