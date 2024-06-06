package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardsDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

public interface CardService {

    void saveCard(Card card);

}
