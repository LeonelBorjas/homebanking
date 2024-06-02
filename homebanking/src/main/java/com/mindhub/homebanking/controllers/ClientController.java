package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.Implement.ClientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController // Marca la clase como un controlador REST, lo que significa que responderá a las solicitudes HTTP
@RequestMapping("/api/clients")  // estamos asociando las peticiones a esta ruta
public class ClientController {

    @Autowired // Inyección de dependencia
    private ClientRepository clientRepository; // Tira el cablecito para poder usar el clientRepository en el Servlet

    @Autowired
    private ClientService clientService;

    @GetMapping("/") // Anotacion que mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public ResponseEntity<?> getAllClients(){
        return clientService.getClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientsById(@PathVariable Long id){
        return clientService.getClientsById(id);
    }
}
