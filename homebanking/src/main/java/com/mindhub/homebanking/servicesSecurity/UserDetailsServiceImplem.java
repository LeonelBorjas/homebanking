package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //Es un servicio que cumplira un funcion en especial para crear una estancia de nuestro user en la aplicacion
public class UserDetailsServiceImplem implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository; //Inyectamos el repository para poder usarlo

    @Override //Aca sobreescribiremos este metodo para que sea como nosotros queramos
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username); //Se le pasa un email y devuelve un client

        if (client == null){
            throw new UsernameNotFoundException(username);
        }

        return User   // usuario que se guardara en el contexHolder
                .withUsername(username) //email
                .password(client.getPassword()) // del cliente obtengo el password
                .roles("CLIENT") // se le da el rol de client
                .build();
    }
}