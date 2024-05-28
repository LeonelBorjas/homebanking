package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardsDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/current/cards")
    public ResponseEntity<?> getCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<CardDTO> cardsList = cardRepository.findByClient(client)
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());

        if (!cardsList.isEmpty()) {
            return new ResponseEntity<>(cardsList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Client has no cards", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/current/create-card")
    public ResponseEntity<?> createCardForAuthenticatedClient(Authentication authentication, @RequestBody CardsDTO cardsDTO) {
        // Obtener el cliente actualmente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        // Convertir los valores de cardType y cardColor a los tipos de enumeración correspondientes
        CardType cardType = CardType.valueOf(cardsDTO.cardType().toUpperCase());
        CardColor cardColor = CardColor.valueOf(cardsDTO.cardColor().toUpperCase());


        if (client.getCards().size() >= 3) {
            return new ResponseEntity<>("Client already has 3 cards", HttpStatus.FORBIDDEN);
        }

        boolean cardExists = client.getCards().stream()
                .anyMatch(card -> card.getCardType() == cardType && card.getCardColor() == cardColor);

        if (cardExists) {
            return new ResponseEntity<>("Client already has this card, consider requesting a different one", HttpStatus.CONFLICT);
        }


        String cardNumber = generateRandomCardNumber();
        int cvv = (int) (Math.random() * 900 + 100);
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);

        Card newCard = new Card(client, cardNumber, cvv, fromDate, thruDate, cardType, cardColor);
        client.addCard(newCard);
        clientRepository.save(client);
        cardRepository.save(newCard);

        return new ResponseEntity<>("Card created for authenticated client", HttpStatus.CREATED);
    }

    private String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int section = (int) (Math.random() * 9000 + 1000);
            cardNumber.append(section).append("-");
        }
        return cardNumber.substring(0, cardNumber.length() - 1);
    }
}
