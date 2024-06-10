package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.Set;

public interface CardService {

    void saveCard(Card card);

    Set<CardDTO> getCardDTOsByClientEmail(String email);

    boolean existsByNumber (String cardNumber);
}
