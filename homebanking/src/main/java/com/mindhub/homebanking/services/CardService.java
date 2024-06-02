package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.CardsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface CardService {
    ResponseEntity<?> getCards(Authentication authentication);

    ResponseEntity<?> createCardForAuthenticatedClient(Authentication authentication, @RequestBody CardsDTO cardsDTO);
}
