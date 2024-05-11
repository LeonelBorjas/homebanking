package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;

    private String cardName;

    private String number;

    private int securityCode;

    private LocalDate since;

    private LocalDate until;

    private CardType cardType;

    private CardColor cardColor;

    public CardDTO (Card card){
        id = card.getId();
        cardName = card.getCardName();
        number = card.getNumber();
        securityCode = card.getSecurityCode();
        since = card.getSince();
        until = card.getUntil();
        cardType = card.getCardType();
        cardColor = card.getCardColor();
    }

    public Long getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public String getNumber() {
        return number;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public LocalDate getSince() {
        return since;
    }

    public LocalDate getUntil() {
        return until;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }
}
