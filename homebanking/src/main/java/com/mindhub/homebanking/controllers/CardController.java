package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardsDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.Implement.CardServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/cards")
    public ResponseEntity<?> getCards(Authentication authentication){
        Client client = clientService.getClientByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        Set<CardDTO> cardDTOS = cards.stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
        if (!cardDTOS.isEmpty()){
            return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("the client does not have cards", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<?> createCardForAuthenticatedClient(Authentication authentication, @RequestBody CardsDTO cardsDTO) {
        // Obtener el cliente actualmente autenticado
        Client client = clientService.getClientByEmail(authentication.getName());

        // Convertir los valores de cardType y cardColor a los tipos de enumeración correspondientes
        CardType cardType = CardType.valueOf(cardsDTO.cardType().toUpperCase());
        CardColor cardColor = CardColor.valueOf(cardsDTO.cardColor().toUpperCase());


        if (client.getCards().size() >= 3) {
            return new ResponseEntity<>("Client already has 3 cards", HttpStatus.FORBIDDEN);
        }

        if (cardsDTO.cardType() == null || cardsDTO.cardType().isEmpty()) {
            return new ResponseEntity<>("Card type cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (cardsDTO.cardColor() == null || cardsDTO.cardColor().isEmpty()) {
            return new ResponseEntity<>("Card color cannot be null or empty", HttpStatus.BAD_REQUEST);
        }


        if (client.getCards().stream().anyMatch(card -> card.getCardType() == cardType && card.getCardColor() == cardColor)) {
            return new ResponseEntity<>("Client already has this card, consider requesting a different one", HttpStatus.CONFLICT);
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
        clientService.saveClient(client);
        cardService.saveCard(newCard);

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
