package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.DTOs.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> getClients(){
        List<Client> clients = clientRepository.findAll();  //Buscar todos los usuarios

        // Devolver ResponseEntity con la lista de usuarios y el codigo de estado Ok
        return new ResponseEntity<>(clients.stream().map(ClientDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientsById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null); // Buscar Usuario por ID
        if (client == null){  // Si no existe, devolver ResponseEntity con el codigo de estado personalizado
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ClientDTO clientDTO = new ClientDTO(client); // si existe devolver ResponseEntity con el usuarioDTO y el codigo de estado OK
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

}
