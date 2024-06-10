package com.mindhub.homebanking.services.Implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service //@Service se utiliza para indicar que una clase representa la capa de servicios de una aplicación
public class CardServiceImp implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Set<CardDTO> getCardDTOsByClientEmail(String email) {
        Client client = clientService.getClientByEmail(email);
        Set<Card> cards = client.getCards();
        return cards.stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsByNumber (String cardNumber) {
        return cardRepository.existsByNumber(cardNumber);
    }
}


