package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Utils.NumberAccountCard;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.requestBody.CardsDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/clients")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/cards")
    public ResponseEntity<?> getCards(Authentication authentication){
        Set<CardDTO> cardDTOS = cardService.getCardDTOsByClientEmail(authentication.getName());
        if (!cardDTOS.isEmpty()){
            return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The client does not have cards", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<?> createCardForAuthenticatedClient(Authentication authentication, @RequestBody CardsDTO cardsDTO) {
        // Obtener el cliente actualmente autenticado
        Client client = clientService.getClientByEmail(authentication.getName());

        if (cardsDTO.cardType() == null || cardsDTO.cardType().isEmpty()) { //Si alguno de ellos es nulo o está vacío,
            return new ResponseEntity<>("Card type cannot be null or empty", HttpStatus.FORBIDDEN);
        }

        if (cardsDTO.cardColor() == null || cardsDTO.cardColor().isEmpty()) { // Si alguno de ellos es nulo o está vacío,
            return new ResponseEntity<>("Card color cannot be null or empty", HttpStatus.FORBIDDEN);
        }

        // Convertir los valores de cardType y cardColor a los tipos de enumeración correspondientes
        CardType cardType = CardType.valueOf(cardsDTO.cardType().toUpperCase());
        CardColor cardColor = CardColor.valueOf(cardsDTO.cardColor().toUpperCase());


        if (client == null) { //si el cliente no existe
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }


        if (client.getCards().size() >= 3) { //si el cliente ya tiene 3 tarjetas
            return new ResponseEntity<>("Client already has 3 cards", HttpStatus.FORBIDDEN);
        }


        // Verificar si el cliente ya tiene una tarjeta con el mismo tipo y color
        if (client.getCards().stream().anyMatch(card -> card.getCardType() == cardType && card.getCardColor() == cardColor)) { //si el cliente ya tiene una tarjeta con el mismo tipo y color
            return new ResponseEntity<>("Client already has this card, consider requesting a different one", HttpStatus.CONFLICT);
        }


        String cardNumber; //generar el numero de la tarjeta
        do {
            cardNumber = NumberAccountCard.generateRandomCardNumber(); //generar el numero de la tarjeta

        } while (cardService.existsByNumber(cardNumber));

        int cvv = getCvv();
        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);

        Card newCard = new Card(client, cardNumber, cvv, fromDate, thruDate, cardType, cardColor);
        client.addCard(newCard);
        clientService.saveClient(client);
        cardService.saveCard(newCard);

        return new ResponseEntity<>("Card created for authenticated client", HttpStatus.CREATED);
    }

    public static int getCvv() {
        int cvv = (int) (Math.random() * 900 + 100); // quita decimales, 100 y 999
        return cvv;
    }

}