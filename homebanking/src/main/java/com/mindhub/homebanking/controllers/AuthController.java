package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import com.mindhub.homebanking.servicesSecurity.UserDetailsServiceImplem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.mindhub.homebanking.controllers.AccountController.eightDigits;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImplem userDetailsServiceImplem;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(),loginDTO.password())); // usa un autenticaction provaider que genera un usedetails usando el userdetails que yo le paso
            final UserDetails userDetails = userDetailsServiceImplem.loadUserByUsername(loginDTO.email()); // genera el token con las credenciales de usuario
            final String jwt = jwtUtilService.generateToken(userDetails);
            return ResponseEntity.ok(jwt); // respuesta ok y esperamos que nos devuelva el token
        }catch (Exception e){
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterDTO registerDTO){

        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            return new ResponseEntity<>("Email is already registered", HttpStatus.FORBIDDEN);
        }

        if (registerDTO.firstName().isBlank()){
            return new ResponseEntity<>("The firs name  field must not be empty", HttpStatus.FORBIDDEN);
        }
        if (registerDTO.lastName().isBlank()){
            return new ResponseEntity<>("The last name field must not be empty", HttpStatus.FORBIDDEN);
        }
        if (registerDTO.email().isBlank()){
            return new ResponseEntity<>("The email field must not be empty", HttpStatus.FORBIDDEN);
        }
        if (registerDTO.password().isBlank()){
            return new ResponseEntity<>("The password field must not be empty", HttpStatus.FORBIDDEN);
        }


        Client client = new Client(
                registerDTO.firstName(),
                registerDTO.lastName(), registerDTO.email(),
                passwordEncoder.encode(registerDTO.password()));

        String number;
        do {
            number = "VIN-" + eightDigits();
        } while (accountRepository.findByNumber(number) != null);

        Account account = new Account(number, LocalDate.now(), 0.0);
        account.setClient(client);
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);

        return new ResponseEntity<>("Client created", HttpStatus.CREATED);
    }


@GetMapping("/current") //obtener ese usuario logeado
    public ResponseEntity<?> getClient (Authentication authentication){ // ese cliente ya logeado
        Client client = clientRepository.findByEmail(authentication.getName()); // obtener el nombre de ese ususario ya logeado
        return ResponseEntity.ok(new ClientDTO(client));
}

}
