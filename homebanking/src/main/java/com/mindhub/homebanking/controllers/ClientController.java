package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
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

import static java.util.stream.Collectors.toList;

@RestController // Marca la clase como un controlador REST, lo que significa que responderá a las solicitudes HTTP
@RequestMapping("/api/clients")  // estamos asociando las peticiones a esta ruta
public class ClientController {

    @Autowired // Inyección de dependencia
    private ClientRepository clientRepository; // Tira el cablecito para poder usar el clientRepository en el Servlet

    @GetMapping("/") // Anotacion que mapea un tipo de solicitud HTTPS tipo get a la ruta que especifico
    // Esto es un servlet (Es una clase java que se utiliza para procesar las solicitudes de los clientes web y generar respuestas dinámicas)
    public ResponseEntity<?> getClients(){  // El ? puede devolver un string o una lista de cosas
        List<Client> clients = clientRepository.findAll();  //Obtengo una lista de todos los clientes de la base de datos usando el metodo

        List<ClientDTO> clientDTOS = clients
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
        // Convierte la lista de clientes en un stream de elementos, Mapea cada cliente a un objeto ClientDTO y para crear una lista que contiene los elementos del stream.

        if (!clients.isEmpty()){
            return new ResponseEntity<>(clientDTOS, HttpStatus.OK); // si existe devolvera ResponseEntity con el usuarioDTO y el codigo de estado OK
        } else {
            return new ResponseEntity<>("No se encontro al cliente", HttpStatus.NOT_FOUND); // Si no existe, devolver ResponseEntity con el string de estado personalizado
        }
    }

    @GetMapping("/{id}")
    // Esto es un servlet (Es una clase java que se utiliza para procesar las solicitudes de los clientes web y generar respuestas dinámicas)
    public ResponseEntity<?> getClientsById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null); // Buscar cliente  por ID
        if (client == null){  // Si no existe, devolver ResponseEntity con el string de estado personalizado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso No encontrado");
        }
        ClientDTO clientDTO = new ClientDTO(client); // si existe devolver ResponseEntity con el usuarioDTO y el codigo de estado OK
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }
}
