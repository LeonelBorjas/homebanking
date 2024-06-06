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

    @Autowired
    private ClientService clientService;

    @GetMapping("/") // Anotacion que mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    public ResponseEntity<?> getAllClients(){
        List<Client> clients = clientService.getListClients();
        List<ClientDTO> clientDTOS = clientService.getListClientsDTO();
        if (!clients.isEmpty()) {
            return new ResponseEntity<>(clientDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The client was not found", HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientsById(@PathVariable Long id){
        Client client = clientService.getClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
        }
        ClientDTO clientDTO =clientService.getClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }
}
