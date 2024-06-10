package com.mindhub.homebanking.utils.testRepos;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class ClientTest {
    @Autowired
    ClientRepository clientRepository;


    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void existClientWithEmail() {
        Client client = clientRepository.findByEmail("leonelborjas@outlook.es");
        assertThat(client, is(notNullValue()));
        assertThat(client.getEmail(), is("leonelborjas@outlook.es"));
    }

    @Test
    public void canAddCards(){
        Client client = new Client("Melba", "Morel", "melba_morel@email.com", "123");
        client.addCard(new Card());
        assertThat(client.getCards(), hasSize(1));
    }

    @Test
    public void clientEmailIsNotNull(){
        Client client = new Client("leonel", "borjas", "leonelborjas@outlook.com", "123");
        assertThat(client.getEmail(), is(notNullValue()));
    }

}
